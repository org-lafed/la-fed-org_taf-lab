# Roadmap

La reconstruction prévue est découpée en lots simples et séquentiels.

## Lots

1. `foundation`
   remettre en place configuration, primitives de base, conventions et règles de qualité
2. `ui-core`
   réintroduire driver bootstrap, pages abstraites, composants communs et waits génériques
3. `api-core`
   réintroduire clients, modèles, assertions et contrat de données génériques
4. `e2e`
   composer des parcours bout en bout seulement après stabilisation UI et API
5. `reporting`
   rebâtir captures, logs, hooks d'exécution et publication de rapports
6. `ci-hardening`
   enrichir les workflows seulement quand les suites réelles existent

## Règle directrice

Chaque lot doit rester petit, vérifiable, et sans surpromesse documentaire.
