# NEETER

# URUCHOMIENIE

#### 1. ANTLR
- Pobranie ANTLR (antlr-4.9.3-complete.jar) https://www.antlr.org/download/
- Import biblioteki w Intellij ProjectStructure -> import ../antlr-4.9.3-complete.jar
- Pobranie ANTLR plugin https://github.com/antlr/antlr4/blob/master/doc/getting-started.md
- Import w Intellij Settings -> Plugins -> Install Plugin from Disk

#### 2. Ustawienie lokalizacji, w której będą generowane pliki języka
![img.png](img/antlr_configure.png)
![img_1.png](img/antlr_path.png)

#### 3. Generujemy pliki języka
![img.png](img/antlr_generate.png)

#### 4. Punkty 2 i 3 powtarzamy dla PreeterLexer.g4 i PreeterParser.g4

## TODO:
- [x] Napisanie gramatyki Neeter:
    - [x] Formulas and text
    - [x] Styles scopes (preset styles)

- [ ] Napisanie gramatyki Preeter:
    - [x] Variable declarations
    - [x] Variable assignments
    - [x] Function declarations
    - [x] Function calls
    - [x] Code scopes
    - [ ] Conditional statement (equivalent to if)
    - [ ] Loop / iteration (equivalent of for / while)
    - [ ] User-friendly error messages

- [ ] Preeter Engine: 
    - [ ] Passing variables by value/ref
    - [ ] While loop
    - [ ] If statement

- [ ] Optional:
    - [ ] Additional stylings per scope
    - [ ] Declaring new style classes
