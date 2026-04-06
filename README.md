# la-fed-org_taf-lab

Ce dépôt a été volontairement ramené à une ossature architecturale neutre et compilable. Il ne contient plus de logique métier, de scénarios concrets, de sélecteurs site-spécifiques, de flux UI/API/E2E implémentés, ni de correctifs opportunistes.

## Intention

Le projet sert désormais de point de départ propre pour une reconstruction pilotée. Le but est de fournir une séparation des responsabilités claire, quelques classes socles minimales et une structure de travail stable pour réintroduire ensuite les capacités UI, API, E2E, reporting et CI de manière contrôlée.

## Ce qui est présent

- un `pom.xml` minimal pour compiler la structure de test
- une arborescence `src/test/java/com/lafed/taf/...` organisée par couches
- des classes de base génériques pour configuration, driver, HTTP, assertions, état et tests
- des fichiers de configuration environnementale avec valeurs placeholders
- des suites TestNG placeholders explicites
- des workflows GitHub réduits à un contrôle de compilation structurelle
- une documentation réécrite pour refléter honnêtement l'état de scaffold

## Ce qui n'est pas implémenté

- aucun scénario UI
- aucun scénario API
- aucun scénario E2E
- aucune intégration métier de site cible
- aucune stratégie de contournement publicitaire
- aucune promesse de reporting final opérationnel
- aucun flux de navigation ou appel API concret

## Structure cible

```text
la-fed-org_taf-lab/
├── pom.xml
├── README.md
├── .gitignore
├── .github/workflows/
├── docs/
└── src/test/
    ├── java/com/lafed/taf/
    └── resources/
```

Les packages `config`, `core`, `ui`, `api`, `data`, `assertions` et `tests` existent uniquement comme scaffold technique.

## Philosophie de reconstruction

1. poser d'abord les fondations transverses
2. réintroduire ensuite les briques UI et API génériques
3. ajouter les scénarios réels seulement après validation explicite
4. brancher le reporting et la CI finale en dernier

## Compilation de la structure

```bash
mvn clean test-compile -Denv=local
```

La commande ci-dessus valide uniquement la cohérence de compilation du scaffold.
