# Test Strategy

La stratégie cible reste volontairement à haut niveau. Le dépôt ne contient pas encore de campagne UI, API ou E2E implémentée.

## Objectif

Reconstruire une pyramide de tests progressive:

- base technique commune
- noyau UI générique
- noyau API générique
- enchaînements E2E limités et justifiés
- reporting et observabilité stabilisés ensuite

## Positionnement actuel

- les tests concrets ont été retirés
- les classes de test restantes sont des placeholders explicites
- les suites XML servent uniquement de structure versionnée
- les workflows CI valident seulement la compilation du scaffold

## Règles de reconstruction

- ajouter les scénarios un par un
- garder les couches découplées
- introduire les données métier au plus tard possible
- ne pas maquiller des placeholders en scénarios implémentés
