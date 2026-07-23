# plusminus-admin
Simple out-of-the-box admin page implementation.

## Admin panel runtime generator

Generates CRUD admin panel for provided classes.

### Configuring admin types

Annotate a class with `@Admin` to make it appear in the admin panel:

```java
@Admin
public class Article {
    ...
}
```

The annotated classes are discovered on startup by classpath scanning. The discovery scope is
the auto-configuration packages (the package of the `@SpringBootApplication` class and everything
below it) plus any packages declared with `@EntityScan`. The annotation has two attributes:

* `api` — custom API URI of the type. When empty (the default), the URI is derived from the type
  name: `${api.prefix:/api}/` followed by the pluralized kebab-case type name
  (e.g. `Article` -> `/api/articles`).
* `order` — order of the type's tab on the admin panel: lower values come first, ties are broken
  by the type name.

For types that cannot be annotated (e.g. classes coming from a third-party jar) or to override
the generated settings of a discovered type, define an `AdminTypesContributor` bean:

```java
@Bean
public AdminTypesContributor adminTypes(ParseService parseService) {
    Type type = parseService.parse(ExternalEntity.class);
    return () -> Collections.singletonList(AdminTypeConfig.builder(type)
            .tableSettings(TableSettings.builder()
                    .fields(Arrays.asList("id", "name"))
                    .build())
            .build());
}
```

The contributed configs are merged with the discovered ones by type name, and a contributed
config **replaces** a discovered one with the same type name, so a contributor is also the way
to customize the table/modal settings or the order of an `@Admin` annotated type.
With no annotated classes and no contributors the application still boots and renders
an empty admin panel.

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
The admin controller is annotated with `@Role("admin")` from plusminus-authorization: when the
plusminus-security/plusminus-authorization stack is present in the application, only authenticated users
with the `admin` role can open the page. The dependency is optional, so without that stack the annotation
is **not** enforced. To avoid an accidental security hole there is SecurityHoleDetector class: on startup
it calls the uri AdminController is mapped to — once without a user and once with a generated token of
a user without roles. If any response is successful (2xx), the page is exposed and the application
fails to start; denials (4xx) and redirects away (3xx) are considered secure. When no TokenProcessor
bean is available the role probe is skipped and a warning is logged, because the role-escalation
scenario cannot be verified. If the probe requests cannot be performed at all (e.g. the
server address is not connectable), a warning is logged and the application starts normally.

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
