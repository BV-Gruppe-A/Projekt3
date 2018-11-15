# Bildverarbeitung Projekt 3: Histogramm, Grauwertwandlung, Momente

Verantwortlich: [Torsten Rauch](https://github.com/ToRauch), [Julian Knaup](https://github.com/julianknaup), [Alissa Müller](https://github.com/chaosbambi) & [Jan-Philipp Töberg](https://github.com/Janfiderheld)

Projektbetreuer: Dipl.-Inform. Jan Leif Hoffmann & Prof. Dr.-Ing. Volker Lohweg 


Startdatum: 14.11.2018

Enddatum & Vorstellung: 28.11.2018

## Aufgabenbeschreibung:

### Teil A) Histogramm

- Erstellen Sie ein Java-Plugin für ImageJ
- Erstellen Sie eine eigene Methode, die für ein Grauwertbild das zugehörige Histogramm ermittelt

### Teil B) Grauwertwandlung

- Wandeln Sie mindestens das gruppenspezifische Farbbild in ein Grauwertbild, zum Einen als Intensitätsbild, zum anderen auf übliche Art und Weise
- Lassen sich die unterschiedlichen Weisen zur Grauwertwandlung im Histogramm wiederfinden? Diskutieren Sie das Ergebnis!

### Teil C) Momente

- Berechnen Sie die statistischen Momente 1 bis 4 sowie die Entropie der Bilder anhand des Histogramms. Welche Aussagen lassen sich treffen? Begründen Sie!
- Wie verhalten sich Momente und Entropie, wenn Sie
  - additives Gauß-Rauschen (mittelwertfrei, σ = 10) auf die Texturen geben?
  - Salz-und-Pfeffer-Rauschen auf die Texturen geben?
  
### Teil D) Präsentation

- Präsentieren Sie Ihr Ergebnis (12 min)
- Denken Sie daran, Gleichungen und Visualisierungen geeignet einzusetzen

### Hinweis

Verwenden Sie die Java-Methode double Random.nextGaussian(), die Gauß-verteilte Zufallswerte mit μ = 0 und σ = 1 erzeugt, und skalieren Sie diese auf die gewünschten Pixelwerte.
