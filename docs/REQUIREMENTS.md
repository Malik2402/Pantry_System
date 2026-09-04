# Requirements and implementation plan

Student: Preshant Ramdhani, 402410438. Module: Mobile App Development 700. Report date: 5 September 2026.

The supplied assignment brief Sections 2–7 is authoritative; the detailed request specifies exactly 20 recipes, SQLiteOpenHelper, Java/XML and five Activities. The brief's four-screen wording also names Settings, so five screens are required. The main narrated video is planned at 6:30 because assessment may stop at seven minutes, even if extra material is recorded.

| Section | Requirement | Location | Verification/evidence |
|---|---|---|---|
| 2 | Persistent pantry CRUD, quantity/unit/expiry | data/DatabaseHelper, ui/IngredientFormActivity | Device CRUD and reopen; screenshots |
| 2 | Twenty seeded recipes and full details | data/RecipeSeeder, recipe tables | Instrumented counts, reopen without duplicates |
| 2.3 | Every ingredient sufficient, normalized and compatible | matching/RecipeMatcher and unit helpers | Unit tests; ingredient addition/removal demonstration |
| 2 | Zero-match feedback | ui/SuggestionsActivity | Empty/insufficient pantry screenshots |
| 3.1 | Five Java/XML screens, Intents, custom RecyclerView adapters, navigation | ui, adapter, res/layout | Build, navigation and missing-record checks |
| 3.1 | Validation, legibility and lifecycle refresh | IngredientFormActivity, layouts, onResume | Invalid forms, rotation, small-screen inspection |
| 3.2 | SQLiteOpenHelper and persistent CRUD | data/DatabaseHelper | Device database and force-stop/reopen tests |
| 3.3 | No maps, GPS, payments or publishing | manifest/dependencies/source | Static audit |
| 4 | Public Git from start, 10+ genuine commits, README | origin, Git history, README | Verify every push and commit purpose |
| 5 | Narrated video, repository, app, three concepts and SQLite justification | docs/VIDEO_SCRIPT.md | User records actual app/code at 1080p where possible |
| 6 | Nine report sections, actual outputs, diagrams, 3–5 snippets, real reflection, references | MobileAppDev700_Assignment.docx | Render and inspect; genuine screenshot checklist |
| 7 | Full source, report and video inside named ZIP below 50 MB | MobileAppDev_Assignment.zip | Contents, size and secret audit; only after video supplied |

Navigation: Pantry ↔ Suggestions ↔ Settings via toolbar; Pantry → Add/Edit; Suggestions → Recipe Detail. Explicit Intents carry IDs only.

Schema: pantry_items(id PK, name, quantity, unit, expiry nullable); recipes(id PK, name, steps); recipe_ingredients(id PK, recipe_id FK → recipes.id, name, quantity, unit). Settings use SharedPreferences.

Packages: ui (five screens and shared navigation), adapter (custom list adapters), model (plain Java data), data (SQLite and seeds), matching (normalization/conversion/matching). XML resources in res; JVM tests in src/test and Android tests in src/androidTest.

Phases: initialization; theme/navigation; schema; CRUD; adapter; validation; recipe seeds; matching; unit tests; recipe screens; persistent settings; reliability; documentation; final testing. Each phase is reviewed and tested before a granular commit and verified push. No history rewriting or artificial commits.

Verification: JVM boundary tests; Android SQLite and preference tests; real emulator interactions/screenshots when available; lint and build; final source/history/package audits. Unperformed manual checks remain explicitly unverified. Recording personal narration, reviewing personal reflection and uploading the submission require the student.
