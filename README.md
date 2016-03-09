# ProteinQuantifier
Protein quantifier tool for peptide to gene and protein uniqueness

Options:

-a, --threads, The amount of threads to use. (default = 4 threads)

-b, --max_proteins  The maximum number of matches a peptide can have before it is seen as an excessive matching
peptide. (default = 100)

-c, --result_dir  The path to the directory to place the results in. (default = ./)

-d, --digestion  The type of digestion to use, options:</br>
0: No digestion (default)</br>
1: Trypsin Conservative</br>
2: Trypsin</br>
3: Pepsin high PH (PH > 2)</br>
4: Pepsin (PH 1.3)</br>
5: Chemotrypsin high specificity</br>
6: Chemotrypsin low specificity

-e, --min_peptide_length
The minimal length a peptide should have after digestion to store it.

-f, --database
The database to digest and match against

-g, --peptides
A set of peptides given through the commandline. (e.g. -g peptide1 peptide2 ...)
Maximum of 50 peptides.

-h, --help
Help function, shows all options

-o, --file_options
Choose which output you want: (faster if only one is chosen)
g: match peptides and give gene output.
p: match peptides and give protein output.
b: match peptides and give both output (default)

-p, --peptide_file  Give peptides to match for in a seperate file.
The file should have each peptide on a seperate line.
