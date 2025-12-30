# Tibetan Translation Tool

The Tibetan to English Dictionary and Translation Tool is one of several tools that can be built from this **Java Tools for Tibetan Text Processing** repository. The Translation Tool takes Tibetan language passages – which can be cut and pasted in, typed in Wylie transliteration, or typed in Tibetan script – and breaks down the passages into their component phrases and words, and displays corresponding dictionary definitions. The Tibetan Translation Tool can be used [online on THL](ttt.thlib.org) without any installation. However, with some simple installation, you can also run it accessing the dictionary online with more advanced functionality, such as typing in Tibetan script instead of roman script transliteration. In addition, it is also possible to install the software on your own computer and then run the Tibetan Translation Tool offline without an Internet connection.

The tool was developed and implemented in its current state by Andrés Montano Pellegrini at the University of Virginia, while the Tibetan script input facility was built by Edward Garrett while working at the University of Virginia.

This tool partially automates the process of translation by breaking up a sentence/paragraph entered in Extended Wylie or Tibetan script into the biggest component parts it can find in multiple dictionary databases. Then for each component part found, it displays its stored definitions and relevant information. This will thus often yield only the definition of a long phrase, rather than its component words, but one can also search for the syllables of that phrase one by one separately. In the Tibetan language, the boundaries of individual words are not marked in any manner such as the way in which spaces separate and mark words in English. Instead, there is a punctuation mark called a "tsheg" which separates each syllable. Thus while syllabic boundaries are utterly explicit, word boundaries are often unclear. One of the main difficulties beginning students thus have with translating Tibetan texts is figuring out where each word ends and the next word starts, and determining what series of syllables to look up in the dictionary either as constituting a single word or a larger compound phrase. This entails a very time consuming process of looking up multiple combinations of syllables to determine which are found within a given dictionary. 


## Running Translation Tool from your device

### Accessing the Translation Tool as Webapp

Visiting [ttt.thlib.org](ttt.thlib.org) is the easiest way to access the Translation Tool from any device that is connected to the Internet.

### Accessing the Translation Tool online dictionaries through a Java program running locally

1. If you want to run the Translation Tool that is able to run a Java virtual machine, make sure you have a Java Runtime Environment installed on your device. It may come pre-installed or you may need to [download](https://www.java.com/en/download/manual.jsp) and install it. 
2. Download the [Stand-alone version of the Translation Tool](https://raw.githubusercontent.com/tibetan-nlp/ttt/master/dist/lib-vanilla/DictionarySearchStandalone.jar), save to your desired folder location
3. Double-click the DictionarySearchStandalone.jar to run.
4. In the welcome window, select "Access an on-line dictionary" and click Ok. If the welcome windows does not load automatically, you can access it with the menu File -> Open...

### Accessing the Translation Tool offline through a Java program running locally
1. Follow the first two steps above to make sure you have Java Runtime Environment and the [Stand-alone version of the Translation Tool](https://raw.githubusercontent.com/tibetan-nlp/ttt/master/dist/lib-vanilla/DictionarySearchStandalone.jar) installed on your device.
2. Download the following three dictionary files: [thl.def](https://raw.githubusercontent.com/tibetan-nlp/ttt/master/dist/data/thl.def), [thl.dic](https://raw.githubusercontent.com/tibetan-nlp/ttt/master/dist/data/thl.dic), and [thl.wrd](https://raw.githubusercontent.com/tibetan-nlp/ttt/master/dist/data/thl.wrd) and save to the same folder.
3. Double-click the DictionarySearchStandalone.jar to run.
4. In the welcome window, select "Access a local dictionary", click Browse to select the thl.wrd file downloaded in the previous step, and click Ok. If the welcome windows does not load automatically, you can access it with the menu File -> Open...

