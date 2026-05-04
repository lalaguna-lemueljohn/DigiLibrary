# DigiLibrary POS (Java Swing)

A desktop Point of Sale (POS) system for a bookstore/library built with pure Java Swing.

## Tech Constraints
- No JDBC / MySQL.
- No external libraries.
- Local persistence through Java Object Serialization in `pos_data.ser`.

## Project Structure

```text
src/
  com/bookstore/main/Main.java
  com/bookstore/model/
  com/bookstore/util/
  com/bookstore/ui/
```

## Requirements
- Java 17+ (Java 11+ should also work)
- `javac` and `java` available in PATH

## Compile
From project root:

```bash
javac $(find src -name '*.java')
```

## Run
From project root:

```bash
java -cp src com.bookstore.main.Main
```

On first run, the app auto-creates `pos_data.ser` with seed data:
- 2 users
- 2 suppliers
- 2 customers
- 16 books across 4 categories

## Default Login Accounts
- **Admin**: `admin` / `admin123`
- **Cashier**: `cashier` / `cash123`


## Assets Folder Names and Placement
Place these folders **inside your project root** (same level as `src`):

```text
DigiLibrary/
  src/
    assets/
      covers/        <- put all book cover PNG/JPG files here
      themes/        <- put theme JPG files here
```

- Book cover folder name: `covers`
- Theme folder name: `themes`
- For dark mode, expected theme filename: `starry_night.jpg` inside `src/assets/themes/`

## Where to Place Images

### Book cover images (PNG/JPG)
Place your book cover **PNG or JPG** files in:

```text
src/assets/covers/
```

Expected seeded filenames:

- `the_hobbit.jpg`
- `dune.jpg`
- `mistborn.jpg`
- `name_of_the_wind.jpg`
- `clean_code.jpg`
- `effective_java.jpg`
- `design_patterns.jpg`
- `refactoring.jpg`
- `sapiens.jpg`
- `guns_germs_steel.jpg`
- `spqr.jpg`
- `the_silk_roads.jpg`
- `atomic_habits.jpg`
- `deep_work.jpg`
- `think_again.jpg`
- `grit.jpg`

If a cover image is missing, the app falls back to a safe placeholder panel with the book title.

### Dark theme background image
Place the Starry Night theme background at:

```text
src/assets/themes/starry_night.jpg
```

If missing, dark mode falls back to a solid dark blue background.

## Receipts
After checkout, receipt files are generated in the project root:

```text
receipt_[timestamp].txt
```

## Data File
Serialized app data is stored in project root:

```text
pos_data.ser
```

Delete this file to reset to default seeded data on next run.
