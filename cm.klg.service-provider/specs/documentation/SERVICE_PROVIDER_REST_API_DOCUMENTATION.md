# API REST du microservice `service-provider`

Ce document décrit les routes REST, leurs paramètres et les DTOs de requête/réponse exposés par le microservice. Le
contrat OpenAPI est défini dans [`main.yml`](../openapi/inbound/main.yml). Les réponses et validations doivent rester
cohérentes avec ce contrat et les contrôleurs.

## Rôle du microservice et opérations métier

Le microservice gère le cycle de vie métier du prestataire dans Z-Link : sa candidature, son profil professionnel, les
services qu'il propose, son portfolio et sa visibilité dans la recherche. Il organise également les revues
administratives de candidature et de nouvelle photo.

`ServiceProvider` est l'Aggregate Root. Les documents d'identité sont des références Media privées dans
`IdentityVerification`; les fichiers ne sont ni reçus ni stockés ici. Les informations personnelles d'utilisateur, dont
le nom, l'email et la date de création, sont gérées par UAM et lues comme une projection utilisateur. Elles ne
constituent pas des champs métier copiés dans l'agrégat du prestataire.

Les opérations métier prises en charge sont :

1. **Créer une candidature** — vérifier l'existence de l'utilisateur et du type de service, empêcher une seconde
   candidature ou un téléphone déjà utilisé, créer le prestataire `PENDING` avec CNI recto/verso, photo de profil et
   service initial, puis publier l'événement de création.
2. **Consulter les prestataires** — lister et filtrer par statut pour l'administration, rechercher les prestataires
   approuvés selon le type de service et la localisation, et fournir une fiche publique détaillée.
3. **Décider d'une candidature** — un administrateur approuve ou rejette une demande avec un `RejectionReason`.
   L'approbation active le statut du prestataire et synchronise son rôle utilisateur; les décisions publient des
   événements destinés aux autres services.
4. **Consulter et corriger son profil** — exposer l'état courant du workflow; refuser les changements pendant l'examen
   d'une candidature; permettre à une candidature rejetée d'être corrigée et resoumise avec les pièces requises;
   autoriser un prestataire approuvé à modifier les champs de profil prévus.
5. **Modérer une nouvelle photo** — garder l'ancienne photo publique lorsqu'un prestataire approuvé soumet une nouvelle
   image; fournir à l'administration une file paginée et un dossier de revue avec profil, photos et références CNI
   privées; à l'approbation, activer la nouvelle image; au rejet, conserver l'ancienne et laisser le prestataire actif.
   La décision est publiée sous forme d'événement.
6. **Gérer les services fournis** — associer un type du catalogue au prestataire avec l'expérience initiale et son
   justificatif, consulter ses associations et en retirer une. Il n'existe pas d'endpoint de mise à jour d'un service
   fourni. Les noms/catégories viennent du catalogue; les années d'expérience sont des données métier, pas des champs du
   profil général.
7. **Gérer le portfolio** — ajouter, modifier, consulter ou supprimer les références Media des réalisations du
   prestataire; publier les événements métier associés aux changements.
8. **Gérer les favoris** — permettre à un utilisateur d'ajouter/supprimer des prestataires approuvés de ses favoris et
   de consulter sa liste paginée; interdire de mettre son propre profil en favori.
9. **Fournir le catalogue** — retourner la liste des types de service actifs, regroupés par catégorie, pour alimenter
   les formulaires et la recherche.
10. **Synchroniser les données interservices** — consommer les événements UAM de création, modification et suppression
    d'utilisateur afin de maintenir la projection locale; enregistrer aussi la relation prestataire-client quand une
    demande de service est acceptée. Ces traitements sont des consommateurs d'événements, pas des endpoints REST listés
    ci-dessous.

Les opérations 1 à 9 correspondent aux capacités REST détaillées ensuite. Les contrôles d'accès, paramètres, DTOs et
codes HTTP de chaque route sont donnés dans les tableaux qui suivent.

## Sécurité

| Accès                             | Routes                                                                                                                                                                                                           |
|-----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Public                            | `GET /service-catalog`, `GET /service-provider/search`, `GET /service-provider/{serviceProviderId}`, `GET /service-provider/{serviceProviderId}/portfolio`, `GET /service-provider/{serviceProviderId}/services` |
| Utilisateur authentifié           | Création et gestion de son profil, de ses services et de son portfolio ; gestion de ses favoris                                                                                                                  |
| Scope `service-provider:read:all` | Liste des prestataires et file/détail de revue des photos                                                                                                                                                        |
| Scope `service-provider:approve`  | Approbation d'une demande ou d'une photo                                                                                                                                                                         |
| Scope `service-provider:reject`   | Rejet d'une demande ou d'une photo                                                                                                                                                                               |

Les routes de revue photo retournent les références privées des CNI. Elles sont réservées aux administrateurs et ne
doivent pas être utilisées dans les profils publics ou utilisateur. Les endpoints publics n'authentifient pas
l'appelant : ils ne retournent donc jamais le téléphone du prestataire ni l'e-mail de l'utilisateur, même si la requête
contient un token.

## Routes

### Prestataires et candidatures

| Méthode et route                                    | Rôle et paramètres                                                                                                                                                                                                                                                                                              | Corps                                                                         | Réponse                                                                                                                                       |
|-----------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| `POST /service-provider`                            | Créer une candidature de prestataire pour l'utilisateur connecté. Authentification requise.                                                                                                                                                                                                                     | `ServiceProviderRegister`                                                     | `201 CreationResponse`; `400` données invalides; `404` utilisateur/type de service absent; `409` prestataire déjà existant ou conflit métier. |
| `GET /service-provider`                             | Lister les prestataires côté administration. Query : `status` optionnel (`ServiceProviderStatus`), `page` optionnel (index à partir de 0), `limit` requis (minimum OpenAPI : 10). Scope `service-provider:read:all`.                                                                                            | Aucun                                                                         | `200 ServiceProviderPaginate`; `400` pagination invalide.                                                                                     |
| `GET /service-provider/search`                      | Rechercher les prestataires approuvés selon le service et la localisation. Query : `serviceTypeId` et `cityId` requis; `districtId`, `quarterId`, `page` optionnels; `limit` requis (minimum OpenAPI : 10). La priorité de localisation est quartier, district, ville. Aucun téléphone ni e-mail n'est renvoyé. | Aucun                                                                         | `200 ServiceProviderPaginate`; `400` paramètres invalides. Route publique.                                                                    |
| `GET /service-provider/{serviceProviderId}`         | Obtenir le profil public détaillé d'un prestataire approuvé. Path : `serviceProviderId` UUID. Aucun téléphone ni e-mail n'est renvoyé; l'appelant n'est pas identifié par cet endpoint public.                                                                                                                  | Aucun                                                                         | `200 ServiceProviderPublicProfile`; `404` prestataire absent/non visible. Route publique.                                                     |
| `PUT /service-provider/{serviceProviderId}/approve` | Approuver une candidature. Path : `serviceProviderId` UUID. Scope `service-provider:approve`.                                                                                                                                                                                                                   | Aucun                                                                         | `204`; `404` prestataire absent; `409` statut incompatible avec l'approbation.                                                                |
| `PUT /service-provider/{serviceProviderId}/reject`  | Rejeter une candidature avec une raison métier structurée. Path : `serviceProviderId` UUID. Scope `service-provider:reject`.                                                                                                                                                                                    | Valeur JSON de type `RejectionReason` (chaîne enum, par ex. `"CNI_INVALID"`). | `204`; `404` prestataire absent; `409` statut incompatible avec le rejet.                                                                     |
| `GET /me/service-provider/application-status`       | Lire l'état de candidature de l'utilisateur sans erreur si aucune demande n'a encore été créée. Appeler cette route pour décider du parcours frontend.                                                                                                                                                     | Aucun                                                                         | `200 MyServiceProviderApplicationStatus`: `NOT_APPLIED`, `PENDING`, `APPROVED` ou `REJECTED`.                                                  |
| `GET /me/service-provider`                          | Lire le profil détaillé et l'état de workflow de l'utilisateur authentifié. À appeler lorsque `application-status` indique une demande existante et que le frontend a besoin de ses informations. Les CNI ne sont jamais retournées ici.                                                                     | Aucun                                                                         | `200 MyServiceProvider`; `404` aucun prestataire pour l'utilisateur.                                                                          |
| `PUT /me/service-provider`                          | Corriger et renvoyer une candidature rejetée. Met à jour le profil et les CNI sur le même aggregate puis repasse le prestataire et la vérification d'identité à `PENDING`. Les services et le portfolio restent inchangés. Les champs de `User` restent gérés par UAM.                                          | `ResubmitServiceProviderApplication`                                          | `204`; `404` prestataire ou utilisateur absent; `400` données/document invalide; `409` statut ou numéro de contact déjà utilisé.              |
| `PUT /me/service-provider/profile`                  | Soumettre une nouvelle photo de profil pour un prestataire approuvé. Ne modifie pas les autres informations du profil ni `IdentityVerification`; la photo reste en attente de revue administrative.                                                                                                         | `UpdateApprovedServiceProviderProfile`                                        | `204`; `404` prestataire ou utilisateur absent; `409` statut incompatible ou prestataire non approuvé.                                        |

Le frontend peut commencer par `GET /me/service-provider/application-status` : `NOT_APPLIED` permet d'afficher le formulaire
de première demande sans appeler le GET détaillé; pour `PENDING`, `APPROVED` ou `REJECTED`, il peut appeler
`GET /me/service-provider` lorsqu'il a besoin des données du profil.

### Revue administrative des photos

Le prestataire soumet uniquement la nouvelle référence `profileImageId` avec `PUT /me/service-provider/profile`. Cette API
ne téléverse pas le fichier : l'image doit déjà exister dans le service Media. La demande apparaît alors dans la file
`GET /service-provider/profile-image-reviews`; l'interface admin peut interroger cette route périodiquement et utiliser
`totalElements` comme compteur. Le contrat ne définit pas de notification push ni d'événement de nouvelle demande. Les
événements sont publiés après la décision d'approbation ou de rejet.

| Méthode et route                                                  | Rôle et paramètres                                                                                                                                                                                                                                                                                           | Corps                          | Réponse                                                                                     |
|-------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|---------------------------------------------------------------------------------------------|
| `GET /service-provider/profile-image-reviews`                     | Lister les demandes en attente (`PENDING_REVIEW`) avec uniquement les champs de résumé du prestataire et de la revue. La file n'inclut ni services, ni `IdentityVerification`; ouvrir le détail pour charger ces données. Query : `page` (défaut 0), `limit` (défaut 20). Scope `service-provider:read:all`. | Aucun                          | `200 ProfileImageReviewPage`, avec `totalElements` et les éléments de file.                 |
| `GET /service-provider/{serviceProviderId}/profile-image-review`  | Charger le dossier complet pour comparer profil, photo courante, photo proposée, services et documents d'identité privés. Path : `serviceProviderId` UUID. Scope `service-provider:read:all`.                                                                                                                | Aucun                          | `200 ServiceProviderProfileImageReview`; `404` prestataire absent ou sans revue compatible. |
| `PUT /service-provider/{serviceProviderId}/profile-image/approve` | Approuver la photo en attente. Elle devient la photo publique courante. Path : `serviceProviderId` UUID. Scope `service-provider:approve`.                                                                                                                                                                   | Aucun                          | `204`; `404` prestataire absent; `409` aucune photo en attente.                             |
| `PUT /service-provider/{serviceProviderId}/profile-image/reject`  | Rejeter la photo en attente. L'ancienne photo reste publique et le prestataire reste actif. Path : `serviceProviderId` UUID. Scope `service-provider:reject`.                                                                                                                                                | Valeur JSON `RejectionReason`. | `204`; `404` prestataire absent; `409` aucune photo en attente.                             |

### Portfolio

| Méthode et route                                      | Rôle et paramètres                                                                                             | Corps                        | Réponse                                                        |
|-------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|------------------------------|----------------------------------------------------------------|
| `POST /service-provider/portfolio`                    | Ajouter un élément au portfolio du prestataire connecté. Authentification requise.                             | `CreatePortfolioItemRequest` | `204`; `404` prestataire absent; `409` conflit d'état.         |
| `GET /service-provider/portfolio`                     | Lire le portfolio du prestataire connecté. Authentification requise.                                           | Aucun                        | `200 PortfolioItem[]`.                                         |
| `GET /service-provider/{serviceProviderId}/portfolio` | Lire le portfolio public du prestataire. Path : `serviceProviderId` UUID.                                      | Aucun                        | `200 PortfolioItem[]`. Route publique.                         |
| `PUT /service-provider/portfolio/{portfolioId}`       | Modifier un élément appartenant au prestataire connecté. Path : `portfolioId` UUID. Authentification requise.  | `CreatePortfolioItemRequest` | `204`; `404` élément/prestataire absent; `409` conflit d'état. |
| `DELETE /service-provider/portfolio/{portfolioId}`    | Supprimer un élément appartenant au prestataire connecté. Path : `portfolioId` UUID. Authentification requise. | Aucun                        | `204`; `404` élément/prestataire absent.                       |

### Services fournis

| Méthode et route                                     | Rôle et paramètres                                                                                                              | Corps                   | Réponse                                                                                                                                                                        |
|------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `GET /service-provider/services`                     | Lister les services du prestataire connecté. Authentification requise.                                                          | Aucun                   | `200 UserService[]`.                                                                                                                                                           |
| `POST /service-provider/services`                    | Ajouter un type du catalogue aux services fournis par le prestataire connecté. Authentification requise.                        | `CreateProviderService` | `201`; `400` données invalides; `404` prestataire ou type absent; `409` service déjà associé/conflit.                                                                          |
| `DELETE /service-provider/services/{serviceTypeId}`  | Retirer un service du prestataire connecté. Path : `serviceTypeId` UUID du type de service catalogue. Authentification requise. | Aucun                   | `204`; `404` prestataire absent (`SERVICE-PROVIDER_404_002`) ou service non associé (`SERVICE-PROVIDER_404_004`); `409` prestataire non approuvé (`SERVICE_PROVIDER-409-004`). |
| `GET /service-provider/{serviceProviderId}/services` | Lister les services publics du prestataire. Path : `serviceProviderId` UUID.                                                    | Aucun                   | `200 UserService[]`; `404` prestataire absent/non visible. Route publique.                                                                                                     |

### Favoris

| Méthode et route                                             | Rôle et paramètres                                                                                                                                    | Corps | Réponse                                                    |
|--------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|-------|------------------------------------------------------------|
| `GET /me/favorites/service-providers`                        | Lister les prestataires favoris de l'utilisateur connecté. Query : `page` optionnel, `limit` requis (minimum OpenAPI : 10). Authentification requise. | Aucun | `200 ServiceProviderPaginate`; `400` pagination invalide.  |
| `POST /me/favorites/service-providers/{serviceProviderId}`   | Ajouter un prestataire aux favoris de l'utilisateur connecté. Path : `serviceProviderId` UUID. Authentification requise.                              | Aucun | `204`; `400` opération invalide; `404` prestataire absent. |
| `DELETE /me/favorites/service-providers/{serviceProviderId}` | Retirer un prestataire des favoris. Path : `serviceProviderId` UUID. Authentification requise.                                                        | Aucun | `204`.                                                     |

### Catalogue

| Méthode et route       | Rôle et paramètres                                                             | Corps | Réponse                                                                                                         |
|------------------------|--------------------------------------------------------------------------------|-------|-----------------------------------------------------------------------------------------------------------------|
| `GET /service-catalog` | Lire le catalogue des types de service, groupé par catégorie. Aucun paramètre. | Aucun | `200` objet dont chaque clé est une catégorie et la valeur une liste de `ServiceCatalogItem[]`. Route publique. |

## DTOs

Les identifiants de médias sont des références vers le service Media. Les fichiers ne sont pas téléversés dans
`service-provider` par ces DTOs.

### Utilisateur et prestataire

| DTO                                    | Rôle et champs principaux                                                                                                                                                                                                                                                                  |
|----------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `UserProfile`                          | Projection utilisateur gérée par UAM, incluse sous `user`: `id`, `firstname` (optionnel), `lastname`, `email` (optionnel), `createdAt`. Le prénom, le nom, l'e-mail et le téléphone du compte ne sont pas modifiables via les routes du prestataire. L'e-mail est omis des vues publiques. |
| `ServiceProviderRegister`              | Demande initiale : `phoneNumber`, `city`, `district`, `quarter`, `serviceType`, `cniRectoId`, `cniVersoId`, `profileImageId` requis; `about` optionnel. La photo de profil sert aussi à vérifier le visage.                                                                                |
| `ResubmitServiceProviderApplication`   | Champs de profil et d'identité modifiables après rejet : `phoneNumber`, `city`, `district`, `quarter`, `about`, `profileImageId`, `cniRectoId`, `cniVersoId`. Les services, le portfolio et les champs de `User` ne sont pas inclus.                                                       |
| `UpdateApprovedServiceProviderProfile` | Référence de la nouvelle photo du profil approuvé : `profileImageId`. Ne modifie ni les autres informations du profil, ni `IdentityVerification`, ni les champs de `User`.                                                                                                                 |
| `MyServiceProvider`                    | Réponse de `/me/service-provider`: `id`, `user`, `status`, `rejectionReason`, `canEdit`, localisation, `about`, `phoneNumber`, image courante/en attente, état de revue photo, statut de vérification et `userServices`. N'inclut aucun identifiant ni document CNI.                       |
| `MyServiceProviderApplicationStatus`    | État succinct pour le frontend : `state` vaut `NOT_APPLIED`, `PENDING`, `APPROVED` ou `REJECTED`. Cet endpoint renvoie toujours `200` pour un utilisateur authentifié.                                                                                                                     |
| `ServiceProvider`                      | Élément de `ServiceProviderPaginate`: `id`, `userId`, `user`, localisation, `about`, dates et `profileImageId`. `phoneNumber` et l'e-mail imbriqué sont absents dans la recherche publique et la liste des favoris; la liste `/service-provider` est admin et conserve les coordonnées.    |
| `ServiceProviderPublicProfile`         | Profil public détaillé : identifiants, `user` sans e-mail, localisation, description, dates, indicateurs client/favori, photo publique, services et portfolio. Le téléphone, le statut interne du prestataire et les données CNI ne sont pas renvoyés.                                     |
| `ServiceProviderPaginate`              | Enveloppe paginée avec `serviceProvider: ServiceProvider[]` et `count`. Le contenu contact dépend de l'endpoint : absent en recherche et favoris, présent dans la liste admin.                                                                                                             |
| `ServiceProviderStatus`                | Statut de candidature/prestataire : `PENDING`, `APPROVED`, `REJECTED`.                                                                                                                                                                                                                     |

### Identité et revue photo

| DTO                                      | Rôle et champs principaux                                                                                                                                                                                                                                                                              |
|------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `RejectionReason`                        | Enum transmis directement comme chaîne JSON. Valeurs OpenAPI : `NAME_MISMATCH`, `CNI_EXPIRED`, `CNI_UNREADABLE`, `CNI_INVALID`, `FACE_MISMATCH`, `FACE_UNCLEAR`, `PHOTO_UNCLEAR`, `DOCUMENT_TAMPERED`, `PROFILE_IMAGE_UNCLEAR`.                                                                        |
| `IdentityVerificationStatus`             | `PENDING`, `APPROVED`, `REJECTED`.                                                                                                                                                                                                                                                                     |
| `ProfileImageReviewStatus`               | `NONE`, `PENDING_REVIEW`, `REJECTED`.                                                                                                                                                                                                                                                                  |
| `ProfileImageReviewPage`                 | File de revue : `totalElements` et `serviceProviders: ProfileImageReviewQueueItem[]`.                                                                                                                                                                                                                  |
| `ProfileImageReviewQueueItem`            | Résumé admin : IDs prestataire/utilisateur, `user`, contact/localisation, statut, image courante, image en attente, statut de revue et `updatedAt` (date de soumission disponible). Ne contient ni services ni `identityVerification`/références CNI; utiliser le endpoint de détail pour ces données. |
| `ServiceProviderProfileImageReview`      | Dossier admin détaillé séparant `serviceProvider`, `profileImageReview` et `identityVerification`.                                                                                                                                                                                                     |
| `ProfileImageReviewProvider`             | Partie profil du dossier détaillé : ID, `userId`, `user`, téléphone, localisation, description, statut, photo courante, services et dates.                                                                                                                                                             |
| `ProfileImageReviewSubmission`           | État de la revue, `pendingProfileImageId`, raison éventuelle de rejet et date de soumission.                                                                                                                                                                                                           |
| `ProfileImageReviewIdentityVerification` | Données privées réservées à la revue admin : statut, `cniRectoId`, `cniVersoId`, motif de rejet d'identité, `verifiedAt`, `verifiedBy`. Les IDs CNI ne sont pas des URLs publiques.                                                                                                                    |

### Services, portfolio et types simples

| DTO                          | Rôle et champs principaux                                                                                                                                                |
|------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `PhoneNumber`                | `countryCode`, `number`.                                                                                                                                                 |
| `ServiceType`                | Type sélectionné lors de l'inscription; contient `id`, `yearOfExperience`, `document` dans le contrat actuel.                                                            |
| `CreateProviderService`      | Association d'un type du catalogue au prestataire : `serviceTypeId`, `yearOfExperience`, `document`.                                                                     |
| `UserService`                | Service fourni en lecture : `serviceName`, `serviceCategory`, `yearOfExperience`, `document`, `createdAt`; le type catalogue peut aussi être présent sous `serviceType`. |
| `ServiceCatalogItem`         | Type catalogue : `id`, `name`, `category`.                                                                                                                               |
| `CreatePortfolioItemRequest` | Création/mise à jour portfolio : `title` (3–100 caractères), `description` (10–500 caractères), `mediaId`.                                                               |
| `PortfolioItem`              | Élément lu : `id`, `serviceProviderId`, `title`, `description`, `mediaId`, `createdAt`.                                                                                  |
| `CreationResponse`           | Réponse de création contenant `newId`.                                                                                                                                   |

## Notes sur le contrat

- Les endpoints publics (`/service-catalog`, recherche, détail, services et portfolio) ne retournent jamais le téléphone
  du prestataire ni l'e-mail utilisateur. La recherche et le détail ne reçoivent pas l'identité de l'appelant; les
  indicateurs `userIsProviderClient` et `userHasFavorited` du détail ne représentent donc pas un utilisateur connecté.
- Les routes publiques ne retournent pas les CNI ni l'objet privé `identityVerification`.
- `GET /me/service-provider` expose l'état de workflow de l'utilisateur, mais pas les références CNI.
- `GET /me/service-provider/application-status` permet de connaître cet état même si aucune candidature n'existe; dans ce cas, `state` vaut `NOT_APPLIED`.
- Les routes `GET .../profile-image-review` et `GET /service-provider/profile-image-reviews` sont des routes admin; la
  réponse de revue admin contient des références de documents privés.
- Le rejet d'une photo ne modifie pas le statut `APPROVED` du prestataire et garde l'image publique actuelle.
- `page` est un index de page commençant à zéro. Les défauts de pagination appliqués côté contrôleur peuvent compléter
  les champs sans défaut explicite dans OpenAPI.
- Pour les codes métier détaillés, voir [`ERROR_CODES.md`](ERROR_CODES.md).
