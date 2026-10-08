# Vues du service Provider

Les vues sont des modèles de lecture utilisés entre les repositories, les cas
d'utilisation et les adaptateurs REST. Elles regroupent les données utiles à
une opération donnée. `UUID` désigne un identifiant; les champs préfixés par
`Nullable` peuvent être absents (`null`).

## Vues du prestataire

### `ServiceProviderView1`

Vue légère utilisée dans les listes, la recherche et les favoris. Elle ne
contient ni services, ni portfolio, ni état ou données de vérification
d'identité.

| Champ             | Type               | Signification                             |
|-------------------|--------------------|-------------------------------------------|
| `id`              | `UUID`             | Identifiant du prestataire                |
| `userId`          | `UUID`             | Identifiant du compte utilisateur associé |
| `user`            | `UserView`         | Informations utilisateur                  |
| `cityId`          | `UUID`             | Identifiant de la ville                   |
| `districtId`      | `UUID`             | Identifiant du district                   |
| `quarterId`       | `Nullable<UUID>`   | Identifiant du quartier                   |
| `approvedBy`      | `Nullable<UUID>`   | Utilisateur ayant approuvé la candidature |
| `rejectedBy`      | `Nullable<UUID>`   | Utilisateur ayant rejeté la candidature   |
| `rejectionReason` | `Nullable<String>` | Motif de rejet de la candidature          |
| `about`           | `Nullable<String>` | Présentation du prestataire               |
| `phoneNumber`     | `PhoneNumber`      | Numéro de contact                         |
| `status`          | `String`           | Statut de la candidature                  |
| `profileImageId`  | `UUID`             | Identifiant du média de profil            |
| `createdAt`       | `LocalDateTime`    | Date de création                          |
| `updatedAt`       | `LocalDateTime`    | Date de dernière mise à jour              |

Opérations principales : `GET /service-provider`,
`GET /service-provider/search` et `GET /me/favorites/service-providers`.
Le mapper REST peut masquer le téléphone ou l'email selon l'opération.

### `ServiceProviderView2`

Vue détaillée du prestataire courant et du détail standard. En plus des champs
de profil, elle contient ses services, son portfolio et l'état de vérification.
`identityVerification` vaut `null` pour un prestataire `APPROVED` ou lorsqu'il
n'existe pas de vérification associée.

| Champ                        | Type                                 | Signification                                                   |
|------------------------------|--------------------------------------|-----------------------------------------------------------------|
| `id`                         | `UUID`                               | Identifiant du prestataire                                      |
| `userId`                     | `UUID`                               | Identifiant du compte utilisateur associé                       |
| `user`                       | `UserView`                           | Informations utilisateur                                        |
| `cityId`                     | `UUID`                               | Identifiant de la ville                                         |
| `districtId`                 | `UUID`                               | Identifiant du district                                         |
| `quarterId`                  | `Nullable<UUID>`                     | Identifiant du quartier                                         |
| `approvedBy`                 | `Nullable<UUID>`                     | Utilisateur ayant approuvé la candidature                       |
| `rejectedBy`                 | `Nullable<UUID>`                     | Utilisateur ayant rejeté la candidature                         |
| `rejectionReason`            | `Nullable<String>`                   | Motif de rejet de la candidature                                |
| `about`                      | `Nullable<String>`                   | Présentation du prestataire                                     |
| `phoneNumber`                | `PhoneNumber`                        | Numéro de contact                                               |
| `status`                     | `String`                             | Statut de la candidature                                        |
| `profileImageId`             | `UUID`                               | Identifiant du média de profil                                  |
| `pendingProfileImageId`      | `Nullable<UUID>`                     | Identifiant de la photo en attente de revue                     |
| `profileImageReviewStatus`   | `ProfileImageReviewStatus`           | Statut de revue de la photo                                     |
| `identityVerificationStatus` | `IdentityVerificationStatus`         | Statut de la vérification d'identité                            |
| `createdAt`                  | `LocalDateTime`                      | Date de création                                                |
| `updatedAt`                  | `LocalDateTime`                      | Date de dernière mise à jour                                    |
| `services`                   | `List<UserServiceView>`              | Services proposés                                               |
| `portfolios`                 | `List<PortfolioView>`                | Éléments du portfolio                                           |
| `identityVerification`       | `Nullable<IdentityVerificationView>` | Vérification d'identité, masquée si le prestataire est approuvé |

Opérations principales : `GET /me/service-provider` et
`GET /service-provider/{serviceProviderId}`.

### `ServiceProviderVerificationView`

Vue dédiée à l'examen de vérification, retournée par
`GET /service-provider/{serviceProviderId}/verification` (scope
`service-provider:verify`). Elle contient les mêmes informations générales,
services et portfolio que la vue détaillée, mais sans champ
`identityVerificationStatus`. Les données `identityVerification` sont omises
pour un prestataire `APPROVED`.

| Champ                      | Type                                 | Signification                                                    |
|----------------------------|--------------------------------------|------------------------------------------------------------------|
| `id`                       | `UUID`                               | Identifiant du prestataire                                       |
| `userId`                   | `UUID`                               | Identifiant du compte utilisateur associé                        |
| `user`                     | `UserView`                           | Informations utilisateur                                         |
| `cityId`                   | `UUID`                               | Identifiant de la ville                                          |
| `districtId`               | `UUID`                               | Identifiant du district                                          |
| `quarterId`                | `Nullable<UUID>`                     | Identifiant du quartier                                          |
| `approvedBy`               | `Nullable<UUID>`                     | Utilisateur ayant approuvé la candidature                        |
| `rejectedBy`               | `Nullable<UUID>`                     | Utilisateur ayant rejeté la candidature                          |
| `rejectionReason`          | `Nullable<String>`                   | Motif de rejet de la candidature                                 |
| `about`                    | `Nullable<String>`                   | Présentation du prestataire                                      |
| `phoneNumber`              | `PhoneNumber`                        | Numéro de contact                                                |
| `status`                   | `String`                             | Statut de la candidature                                         |
| `profileImageId`           | `UUID`                               | Identifiant du média de profil                                   |
| `pendingProfileImageId`    | `Nullable<UUID>`                     | Identifiant de la photo en attente de revue                      |
| `profileImageReviewStatus` | `ProfileImageReviewStatus`           | Statut de revue de la photo                                      |
| `createdAt`                | `LocalDateTime`                      | Date de création                                                 |
| `updatedAt`                | `LocalDateTime`                      | Date de dernière mise à jour                                     |
| `services`                 | `List<UserServiceView>`              | Services proposés                                                |
| `portfolios`               | `List<PortfolioView>`                | Éléments du portfolio                                            |
| `identityVerification`     | `Nullable<IdentityVerificationView>` | Données de vérification, masquées si le prestataire est approuvé |

### `ServiceProviderView3`

Vue complète utilisée pour l'examen d'une photo de profil en attente par
`GET /service-provider/{serviceProviderId}/profile-image-review`. Elle réunit
les données du prestataire, les informations de soumission, les services et
les éléments d'identité nécessaires à la revue.

| Champ                         | Type                        | Signification                                        |
|-------------------------------|-----------------------------|------------------------------------------------------|
| `id`                          | `UUID`                      | Identifiant du prestataire                           |
| `userId`                      | `UUID`                      | Identifiant du compte utilisateur associé            |
| `user`                        | `UserView`                  | Informations utilisateur                             |
| `phoneNumber`                 | `PhoneNumber`               | Numéro de contact                                    |
| `cityId`                      | `UUID`                      | Identifiant de la ville                              |
| `districtId`                  | `UUID`                      | Identifiant du district                              |
| `quarterId`                   | `Nullable<UUID>`            | Identifiant du quartier                              |
| `about`                       | `Nullable<String>`          | Présentation du prestataire                          |
| `status`                      | `ServiceProviderStatus`     | Statut de la candidature                             |
| `profileImageId`              | `UUID`                      | Identifiant de la photo de profil actuelle           |
| `pendingProfileImageId`       | `UUID`                      | Identifiant de la photo en attente                   |
| `profileImageReviewStatus`    | `ProfileImageReviewStatus`  | Statut de revue de la photo                          |
| `profileImageRejectionReason` | `Nullable<RejectionReason>` | Motif de rejet de la photo, le cas échéant           |
| `submittedAt`                 | `Nullable<LocalDateTime>`   | Date de soumission de la photo en attente            |
| `identityVerification`        | `IdentityVerificationView`  | Données de vérification d'identité utiles à la revue |
| `createdAt`                   | `LocalDateTime`             | Date de création du prestataire                      |
| `updatedAt`                   | `Nullable<LocalDateTime>`   | Date de dernière mise à jour                         |
| `services`                    | `List<UserServiceView>`     | Services proposés                                    |

### `ProfileImageReviewSummaryView`

Résumé affiché dans la file `GET /service-provider/profile-image-reviews`.
Il permet de présenter une soumission sans charger les éléments détaillés de
la vérification d'identité.

| Champ                      | Type                       | Signification                             |
|----------------------------|----------------------------|-------------------------------------------|
| `serviceProviderId`        | `UUID`                     | Identifiant du prestataire                |
| `userId`                   | `UUID`                     | Identifiant du compte utilisateur associé |
| `user`                     | `UserView`                 | Informations utilisateur                  |
| `phoneNumber`              | `PhoneNumber`              | Numéro de contact                         |
| `cityId`                   | `UUID`                     | Identifiant de la ville                   |
| `districtId`               | `UUID`                     | Identifiant du district                   |
| `quarterId`                | `Nullable<UUID>`           | Identifiant du quartier                   |
| `status`                   | `ServiceProviderStatus`    | Statut de la candidature                  |
| `currentProfileImageId`    | `UUID`                     | Identifiant de la photo actuelle          |
| `pendingProfileImageId`    | `UUID`                     | Identifiant de la photo à examiner        |
| `profileImageReviewStatus` | `ProfileImageReviewStatus` | Statut de revue de la photo               |
| `submittedAt`              | `Nullable<LocalDateTime>`  | Date de soumission de la photo            |

## Vues d'identité

### `ServiceProviderIdentityView`

Vue intermédiaire chargée pour l'utilisateur courant par le repository. Elle
sert au cas d'utilisation `GET /me/service-provider/identity` pour contrôler
le statut et composer la réponse spécifique aux vérifications rejetées.

| Champ                  | Type                       | Signification                     |
|------------------------|----------------------------|-----------------------------------|
| `providerId`           | `UUID`                     | Identifiant du prestataire        |
| `profileImageId`       | `Nullable<UUID>`           | Identifiant de la photo de profil |
| `identityVerification` | `IdentityVerificationView` | Données de vérification associées |

### `RejectedServiceProviderIdentityView`

Réponse métier destinée à `GET /me/service-provider/identity`. Le cas
d'utilisation ne la retourne que si le statut est `REJECTED`; sinon il lève
`InvalidServiceProviderStatusException`.

| Champ             | Type                         | Signification                                              |
|-------------------|------------------------------|------------------------------------------------------------|
| `providerId`      | `UUID`                       | Identifiant du prestataire                                 |
| `cniRectoId`      | `UUID`                       | Identifiant du document CNI recto                          |
| `cniVersoId`      | `UUID`                       | Identifiant du document CNI verso                          |
| `profileImageId`  | `UUID`                       | Identifiant de la photo de profil                          |
| `status`          | `IdentityVerificationStatus` | Statut de vérification, toujours `REJECTED` pour cette vue |
| `rejectionReason` | `RejectionReason`            | Motif du rejet                                             |

### `IdentityVerificationView`

Représentation en lecture de l'enregistrement de vérification d'identité.
Elle est incluse dans les vues autorisées à traiter cette vérification.

| Champ             | Type                         | Signification                                   |
|-------------------|------------------------------|-------------------------------------------------|
| `id`              | `UUID`                       | Identifiant de l'enregistrement de vérification |
| `cniRectoId`      | `Nullable<UUID>`             | Identifiant du document CNI recto               |
| `cniVersoId`      | `Nullable<UUID>`             | Identifiant du document CNI verso               |
| `status`          | `IdentityVerificationStatus` | Statut de vérification                          |
| `rejectionReason` | `Nullable<RejectionReason>`  | Motif du rejet, le cas échéant                  |
| `verifiedAt`      | `Nullable<LocalDateTime>`    | Date de la décision de vérification             |
| `verifiedBy`      | `Nullable<UUID>`             | Identifiant du vérificateur                     |

## Vues imbriquées

### `UserView`

Informations utilisateur associées aux vues du prestataire. Le mapper REST
contrôle si l'email est inclus dans une réponse.

| Champ       | Type               | Signification              |
|-------------|--------------------|----------------------------|
| `id`        | `UUID`             | Identifiant utilisateur    |
| `firstname` | `Nullable<String>` | Prénom                     |
| `lastname`  | `String`           | Nom                        |
| `email`     | `Nullable<String>` | Adresse email              |
| `createdAt` | `LocalDateTime`    | Date de création du compte |

### `UserServiceView`

Un service proposé par le prestataire.

| Champ              | Type              | Signification                              |
|--------------------|-------------------|--------------------------------------------|
| `serviceType`      | `ServiceTypeView` | Type de service issu du catalogue          |
| `yearOfExperience` | `int`             | Années d'expérience déclarées              |
| `document`         | `UUID`            | Identifiant du document associé au service |
| `createdAt`        | `LocalDateTime`   | Date d'ajout du service                    |

### `ServiceTypeView`

Informations de catalogue imbriquées dans `UserServiceView`.

| Champ      | Type      | Signification                           |
|------------|-----------|-----------------------------------------|
| `id`       | `UUID`    | Identifiant du type de service          |
| `name`     | `String`  | Nom du service                          |
| `category` | `String`  | Catégorie du service                    |
| `isActive` | `boolean` | Indique si le type de service est actif |

### `PortfolioView`

Un élément du portfolio du prestataire.

| Champ         | Type            | Signification            |
|---------------|-----------------|--------------------------|
| `id`          | `UUID`          | Identifiant de l'élément |
| `title`       | `String`        | Titre                    |
| `description` | `String`        | Description              |
| `mediaId`     | `UUID`          | Identifiant du média     |
| `createdAt`   | `LocalDateTime` | Date de création         |

## Projection de persistance

### `ProfileImageReviewView`

Projection JPA utilisée pour lire les éléments de la file de revue photo. Elle
est construite par la requête de persistance et convertie en
`ProfileImageReviewSummaryView`; ce n'est ni une vue applicative ni une réponse
REST.

| Champ                      | Type                      | Signification                               |
|----------------------------|---------------------------|---------------------------------------------|
| `serviceProviderId`        | `UUID`                    | Identifiant du prestataire                  |
| `userId`                   | `UUID`                    | Identifiant utilisateur associé             |
| `userRecordId`             | `UUID`                    | Identifiant de l'enregistrement utilisateur |
| `firstname`                | `Nullable<String>`        | Prénom                                      |
| `lastname`                 | `String`                  | Nom                                         |
| `emailAddress`             | `Nullable<String>`        | Adresse email                               |
| `userCreatedAt`            | `LocalDateTime`           | Date de création du compte utilisateur      |
| `phoneCountryCode`         | `String`                  | Indicatif téléphonique                      |
| `phoneNumber`              | `String`                  | Numéro de téléphone                         |
| `cityId`                   | `UUID`                    | Identifiant de la ville                     |
| `districtId`               | `UUID`                    | Identifiant du district                     |
| `quarterId`                | `Nullable<UUID>`          | Identifiant du quartier                     |
| `providerStatus`           | `String`                  | Statut de la candidature                    |
| `currentProfileImageId`    | `UUID`                    | Identifiant de la photo actuelle            |
| `pendingProfileImageId`    | `UUID`                    | Identifiant de la photo en attente          |
| `profileImageReviewStatus` | `String`                  | Statut de revue de la photo                 |
| `submittedAt`              | `Nullable<LocalDateTime>` | Date de soumission de la photo              |

## Règles de confidentialité

- `ServiceProviderView2` et `ServiceProviderVerificationView` n'exposent pas
  `identityVerification` pour un prestataire `APPROVED`.
- `ServiceProviderView1` et `ProfileImageReviewSummaryView` ne contiennent
  aucune donnée détaillée de vérification d'identité.
- `RejectedServiceProviderIdentityView` n'est retournée que pour une
  vérification au statut `REJECTED`.
- Les contrôles d'accès des endpoints s'appliquent en complément de ces vues;
  la séparation des champs ne remplace pas l'autorisation.
