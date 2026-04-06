# Architecture

Le dépôt est revenu à un état de scaffold. Les couches existent pour cadrer la reconstruction, pas pour fournir un framework métier prêt à l'emploi.

## Couches

- `config`: charge la configuration d'exécution et les propriétés d'environnement.
- `core/driver`: porte la gestion générique du cycle de vie navigateur.
- `core/http`: porte les primitives HTTP partagées et neutres.
- `core/listeners`: réserve pour les hooks d'exécution et d'observabilité.
- `core/utils`: utilitaires transverses sans logique métier.
- `core/state`: stockage léger d'état inter-étapes.
- `ui/pages`, `ui/components`, `ui/flows`: socle UI abstrait sans navigation concrète.
- `api/clients`, `api/services`, `api/models`, `api/assertions`: socle API abstrait sans contrats métier.
- `data/builders`, `data/generators`: génération et assemblage de données génériques.
- `assertions`: assertions transverses non spécifiques.
- `tests/ui`, `tests/api`, `tests/e2e`: points d'entrée placeholders pour futures suites.

## Principes

- aucune URL métier n'est embarquée dans le code
- aucun locator n'est embarqué dans le code
- aucune logique de scénario n'est conservée
- chaque classe a une responsabilité minimale et explicite
- les endroits à compléter plus tard sont marqués avec `TODO`

## État actuel

Cette architecture compile, mais elle ne prétend pas encore exécuter une campagne de test utile. La reconstruction fonctionnelle doit être pilotée couche par couche.
