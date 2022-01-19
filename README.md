<h1 align="center"> Neeter </h1> <br>

## Spis treści
* [Neeter](#neeter)
* [Uruchomienie](#uruchomienie)
* [Język proceduralny Preeter](#język-proceduralny-preeter)
* [Przykłady użycia](#przykłady-użycia)
* [Technologie](#technologie)
* [Zakres funkcjonalności](#zakres-funkcjonalności)
* [Development](#development)

## Neeter
Jest to język przeznaczony do opisu dokumentów w sposób ogólny, dający możliwość proceduralnego generowania zawartości. Środowisko Neeter przetwarza dokument zapisany w naszym języku i generuje dokument w formacie gotowym do wyświetlenia.

Jako podstawowy format wyjściowy użyliśmy dokumentów HTML. Środowisko jest tak przygotowane, że rozszerzenie procesu generacji do innych popularnych formatów (np. `svg` lub `pdf`) nie sprawia problemów.

## Uruchomienie
Uruchomienie pliku .jar z następującymi argumentami
- input - nazwa pliku z kodem w Neeter
- watch (opcjonalne) - obserwuje i kompiluje po każdej zmianie pliku wejściowego (widzimy rezultat w czasie rzeczywistym)
``` 
java -jar neeter.jar ./path/to/inputFile.neet [-watch]
```

## Język proceduralny Preeter
Neeter, podobnie jak język PHP, pozwala na osadzanie kawałków kodu imperatywnego pomiędzy treść dokumentu. Nazywamy ten mini-język "Preeter" (pre-neeter). Wynikiem przetwarzania kodu Preeter jest zawartość zapisana w języku Neeter. Poniższy diagram pokazuje proces przetwarzania kodu źródłowego na dokumenty docelowe.

<br>

![img_5.png](img/neeter_diagram.png)


## Przykłady użycia
Uruchamiając przykład z [examples/example1.neet](examples/example1.neet) otrzymamy następujący rezultat.
<br/>

![img_4.png](img/neeter_example.png)

## Technologie
- Java 8
- Antlr 4.9.3

## Zakres funkcjonalności
### Generowanie zawartości dzięki wstawek Preeter
```
Zawartość dokumentu. 
@{
  // To jest komentarz
  show("Zawartość generowana proceduralnie: ", 56 / 2, ". ");
@}
Dalsza zawartość dokumentu.
```

### Zmienne (w tym zasięgi obowiązywania zmiennych)
```
let x = 5;
let content = "Hello";
let isGood = false;
```
- Typy zmiennych
   - string
   - int
   - boolean

### Operacje arytmetyczne
```
let x = 5;
let y = (x + 5) % 2;
let z = (x * 5) / 2;
```

### Instrukcje warunkowe
```
if (x % 2 == 0) {
    show("even", "\n\n");
}
else if ((i % 2) != 0) {
    show("odd", "\n\n");
} 
else {
    show("?", "\n\n");
}
```

### Pętla while
```
while (i < n) {
    // code
}
```

### Funkcje (również rekurencyjne)
```
def helloNeeter(n, ...) {
    // code
}

helloNeeter(n, ...)
```

```
def fib(n) {
    if (n <= 1) {
        return n;
    }

    return fib(n - 1) + fib(n - 2);
}
```

### Wbudowane funkcje:
- show - Przyjmuje dowolną liczbę argumentów, wypisuje każdy z nich do zawartości dokumentu w kolejności występowania na liście parametrów. Przykłady:
    - `show("Hello", 5)`
    - `show(5 + 6)`
    - `show("Some", " words", " are concatinated")`

### Style scopes
```
#klasa[parametr1=wartość1, parametr2=wartość2, ..., parametrN=wartośćN] {
    ...Zawartość dokumentu
}

```
  - rozmiar tesktu w px
  ```
  [size=10]
  ```

  - kolor
  ```
  [color=#123abc]
  ```
  
  - marginesy pomiędzy akapitami
  ```
  [margin_top=5, margin_bottom=1]
  ```
  
  - wyrównanie
  ```
  [alignment=10]
  ```
  
  - pogrubienie
  ```
  [weight=800]
  ```

### Formatowanie dla formuł matematycznych
```
{{x = 1 + 2 + 4 + ... + 128}}
```

## Development
#### Import
W celu edycji kodu żródłowego niezbędny będzie import kilku bibliotek.
1. ANTLR
    - Pobranie ANTLR (antlr-4.9.3-complete.jar) https://www.antlr.org/download/
    - Import biblioteki w Intellij ProjectStructure -> import ../antlr-4.9.3-complete.jar
    - Pobranie ANTLR plugin https://github.com/antlr/antlr4/blob/master/doc/getting-started.md
    - Import w Intellij Settings -> Plugins -> Install Plugin from Disk <br/><br/>

2. Ustawienie lokalizacji, w której będą generowane pliki języka <br/>
   ![img.png](img/antlr_configure.png) <br/>
   ![img_1.png](img/antlr_path.png)

3. Generujemy pliki języka <br/>
   ![img.png](img/antlr_generate.png)

4. Punkty 2 i 3 powtarzamy dla PreeterLexer.g4 i PreeterParser.g4

5. Uruchom program z argumentem "examples/example1.neet"
