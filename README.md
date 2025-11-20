# snooker
Aplicació Android per a comptar la puntuació dels "frames" de Snooker.

V11
===
    Ja he aconseguit compilar i executar, en el mòbil,  el programari del Git .

V12
===
Els objectius de la V12 són:
- Canviar botó "Pronunciar" per "Final" i que acabi l'aplicació **FET**
- Deixar de mostrar el text reconegut (vermella, groga, falta de ... etc)
- Eliminar botó "Reconèixer" i que reconegui "continuament" el que se li diu.
  - això implica tenir una paraula clau que s'haurà de dir abans del color de la bola o la falta
  - podria ser el nom del jugador que l'ha entrat o ha fet la falta
  - de moment provarem amb una paraula clau, per exemple: "lola" o "nena" o el que se'ns acudeixi
  - provaré de fer diverses paraules clau: "bola", "falta", "canvi". 
  - primer provaré amb "bola"
.

provaré de fer, primer de tot, que reconegui la paraula "bola" o "vola" i retorni
un string amb la resta de paraules reconegudes i a continuació invoqui el procediment actual 
de reconeixemnt de paraules. **FET**
