# PUCK (Peptide Uniqueness ChecKer)
Protein quantifier tool for peptide to gene and protein uniqueness

<h2>Options:</h2>

-a, --threads:</br>
The amount of threads to use. (default = 4 threads)

-b, --max_proteins:</br>
The maximum number of matches a peptide can have before it is seen as an excessive matching
peptide. (default = 100)

-c, --result_dir:</br>
The path to the directory to place the results in. (default = ./)

-d, --digestion:</br>
The type of digestion to use, options:</br>
&emsp;&emsp;0: No digestion (default)</br>
&emsp;&emsp;1: Trypsin Conservative</br>
&emsp;&emsp;2: Trypsin</br>
&emsp;&emsp;3: Pepsin high PH (PH > 2)</br>
&emsp;&emsp;4: Pepsin (PH 1.3)</br>
&emsp;&emsp;5: Chemotrypsin high specificity</br>
&emsp;&emsp;6: Chemotrypsin low specificity

-e, --min_peptide_length:</br>
The minimal length a peptide should have after digestion to store it.

-f, --database:</br>
The database to digest and match against

-g, --peptides:</br>
A set of peptides given through the commandline. (e.g. -g peptide1 peptide2 ...)
Maximum of 50 peptides.

-h, --help:</br>
Help function, shows all options

-o, --file_options:</br>
Choose which output you want: (faster if only one is chosen)</br>
&emsp;&emsp;g: match peptides and give gene output.</br>
&emsp;&emsp;p: match peptides and give protein output.</br>
&emsp;&emsp;b: match peptides and give both output (default)

-p, --peptide_file:</br>
Give peptides to match for in a seperate file.</br>
The file should have each peptide on a seperate line.
