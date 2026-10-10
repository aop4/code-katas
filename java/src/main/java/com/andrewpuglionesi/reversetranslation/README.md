# Reverse gene translation
This problem will require you to think back to high school biology, but I've tried to minimize jargon and keep it simple.

# Context
Each protein in your body is a sequence of amino acid molecules. The 20 types of amino acids are strung together in long chains to make proteins.

For example, a protein might contain the amino acid sequence Serine->Cysteine->Tryptophan, which we can abbreviate as `Ser-Cys-Trp`.

Similarly, an RNA strand is a sequence of small molecules, called nucleotides, which are strung together in a sequence. The four molecules that make up a strand of RNA are adenine, guanine, uracil, and cytosine. These are abbreviated as A, G, U, and C, respectively.

The creation of a protein is a two-step process: a gene in your DNA is transcribed into a strand of RNA, and then the RNA is translated into a protein. In this kata, we'll reverse-engineer the latter process.

The order of amino acids in a protein is determined by the order of nucleotides in the corresponding RNA sequence. For example, the RNA sequence CAG encodes the amino acid glutamine, abbreviated as Gln.

# Problem statement
Given an amino acid sequence from a protein, determine all the RNA sequences that could have generated it.

To help you get started, here is a JSON file that maps each amino acid to the list of RNA sequences that can produce it:

```json
{
  "Phe": ["UUU", "UUC"],
  "Leu": ["UUA", "UUG", "CUU", "CUC", "CUA", "CUG"],
  "Ile": ["AUU", "AUC", "AUA"],
  "Met": ["AUG"],
  "Val": ["GUU", "GUC", "GUA", "GUG"],
  "Ser": ["UCU", "UCC", "UCA", "UCG", "AGU", "AGC"],
  "Pro": ["CCU", "CCC", "CCA", "CCG"],
  "Thr": ["ACU", "ACC", "ACA", "ACG"],
  "Ala": ["GCU", "GCC", "GCA", "GCG"],
  "Tyr": ["UAU", "UAC"],
  "His": ["CAU", "CAC"],
  "Gln": ["CAA", "CAG"],
  "Asn": ["AAU", "AAC"],
  "Lys": ["AAA", "AAG"],
  "Asp": ["GAU", "GAC"],
  "Glu": ["GAA", "GAG"],
  "Cys": ["UGU", "UGC"],
  "Trp": ["UGG"],
  "Arg": ["CGU", "CGC", "CGA", "CGG", "AGA", "AGG"],
  "Gly": ["GGU", "GGC", "GGA", "GGG"]
}
```

# Examples
Input: `"Met-Trp"`  
Output: `["AUGUGG"]`  
Explanation: There is only one way to produce this amino acid sequence because `Met` and `Trp` are each encoded by only one RNA sequence.

Input: `"Tyr-His"`  
Output: `["UAUCAU", "UAUCAC", "UACCAU", "UACCAC"]`
Explanation: Because Tyr and His can each be encoded by two different RNA sequences, there are four different RNA sequences that can generate the amino acid sequence.  
Tyr can be produced with either UAU or UAC, and His can be produced with either CAU or CAC. To get the result, we simply find every combination of these values.
