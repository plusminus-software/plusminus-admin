# plusminus-admin
Simple out-of-the-box admin page implementation.

## Admin panel runtime generator

Generates CRUD admin panel for provided classes.

### Supported operations:

1) Create
2) Update
3) Delete
4) Clone
5) Read (paginated table)

### Form fields

Top level:
* all the fields of current-alevel class (but not parents)
* card with fields of parent class (every parent class has a separate field), able to be hidden/showed

### Supported field types

| Java type |         Single         |         Array         |
| --------- |         ------         |         --            |
| Boolean   | input[type="checkbox"] |          TBD          |
| Number    | input[type="number"]   | taginput + event check|
| String    | input                  |                       |
| Enum      | select                 | bootstrap-select      |
| Embedded  | card with remove btn   | taginput + card&btn   |
| Entity    | card with list&btn     | taginput + list&btn   |
| Parent    | card with fields       | -                     |

## Security
The plusminus-admin is **not** responsible to secure admin's uri in your application. But to avoid accidental 
security hole there is SecurityHoleDetector class which fails the application in case admin URI is not secured.
[Needed to be implemented].

## Upcomming to implement

* @JsonIgnore hides field 
* Immutable fields are readonly on Update
* Number array event checking on add
* Null on a single embedded
* Button to show/hide embedded card on new embedded adding in case list of embeddeds field
* Button to show/hide entity table on new entity adding in case list of entities field
* Entity table in case single entity field
* Disable input in taginput in case embedded/entity list
* Bug - checkbox doesnt work on a second request
* Bug - checkbox doesnt populate correctly
* Bug - incorrect create/update on an empty inputs, deselected checkboxes
* Bug - tagsinput doesnt work

* Low priority:
** Make Relation polymorphic (f. e. if type is Animal on UI it is possible to select Animal, Dog and Cat)
** Make pagination able to handle a big count of pages
** Boolean array type
** SecurityHoleDetector
