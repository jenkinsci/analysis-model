### Vergleich Delegation vs. Vererbung

Vorteil Delegation | Vorteil Vererbung
-------------------|------------------
Nicht alle Methoden müssen implementiert werden | Unveränderte Methoden sind "ausgeblendet"<br> --> übersichtlicher
Vor dem super() Aufruf kann keine andere Logik stehen --> Delegation ist flexibler, wenn die Originalimpelmentierung auch verwendet werden soll | Wenn die Superklasse neue Methoden erhält, erbt die Subklasse sofort deren Implementierung
Als Delegateobjekt kann auch eine Subklasse von ArrayList<T> übergeben werden | Zugriff auf Methoden mit `Protected` Modifier möglich