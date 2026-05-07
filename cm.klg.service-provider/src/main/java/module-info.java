@org.jspecify.annotations.NullMarked
module cm.klg.service_provider.main {
  requires spring.boot;
  requires spring.boot.liquibase;
  requires liquibase.core;
  requires spring.orm;
  requires static lombok;
  requires spring.web;
  requires spring.context;
  requires io.swagger.v3.oas.annotations;
  requires jakarta.validation;
  requires jakarta.annotation;
  requires com.fasterxml.jackson.annotation;
  requires spring.security.web;
  requires spring.security.config;
  requires spring.security.oauth2.resource.server;
  requires spring.security.core;
  requires spring.security.crypto;
  requires spring.security.oauth2.jose;
  requires spring.core;
  requires org.mapstruct;
  requires static org.jspecify;
  requires jakarta.persistence;
  requires spring.data.jpa;
  requires org.slf4j;
  requires spring.data.commons;
  requires common.service.bridge.main;
  requires spring.tx;
  requires spring.boot.persistence;
  requires common.base.main;
  requires com.emb.core;
  requires org.apache.tomcat.embed.core;
  requires spring.boot.autoconfigure;
  requires org.apache.httpcomponents.core5.httpcore5;
  requires org.apache.logging.log4j;
  requires spring.messaging;
}
