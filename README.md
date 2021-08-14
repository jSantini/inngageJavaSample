<h1>Inngage - Plataforma de Engajamento Mobile First</h1>

<p>Com a Inngage você cria facilmente comunicações personalizadas e automatizadas de acordo com o contexto de cada usuário, através dos canais: Push Notifications, SMS, In-app Messages, WhatsApp, entre outros!</p>




[![Latest Github release](https://img.shields.io/amo/v/Badges?logo=inngage&style=plastic)](https://github.com/inngage/inngage-lib/releases/latest)
[![Build status of the master branch on Linux/OSX](https://img.shields.io/travis/Martinsos/edlib/master?label=Linux%20%2F%20OSX%20build)](https://travis-ci.com/Martinsos/edlib)
[![Build status of the master branch on Windows](https://img.shields.io/appveyor/build/Martinsos/edlib/master?label=Windows%20build)](https://ci.appveyor.com/project/Martinsos/edlib/branch/master)
[![Chat on Gitter](https://img.shields.io/gitter/room/Martinsos/edlib.svg?colorB=753a88)](https://gitter.im/Martinsos/edlib)
[![Published in Bioinformatics](https://img.shields.io/badge/Published%20in-Bioinformatics-167DA4.svg)](https://doi.org/10.1093/bioinformatics/btw753)
=====

<h2>Integrando Inngage SDK com aplicativos Android</h2>

<h3>1 - Google Messaging Services & FIREBASE Services</h3>

<p>Antes de implementar a biblioteca, dentro do seu projeto no Android Studio, abra o arquivo build.gradle (Module: app) e adicione as seguintes dependências ao seu projeto.</p>

<h4>1.1 - Implemente essas dependências no arquivo build.gradle (Module: app):</h4>

```cpp
apply plugin: 'com.android.application'
// Adicione esta linha
apply plugin: 'com.google.gms.google-services'

dependencies {
  // Importe o Firebase BoM
  implementation platform('com.google.firebase:firebase-bom:27.1.0')

  // Adicione a dependência para o Firebase SDK para Google Analytics
  // Ao usar o BoM, não especifique versões nas dependências do Firebase
  implementation 'com.google.firebase:firebase-analytics'

  // Adicione as dependências para qualquer outro produto Firebase desejado
  
}
```

<h4>1.2 - Ainda no arquivo build.gradle (Module: app), adicione no final do arquivo o seguinte plugin:</h4>

```cpp
apply plugin: 'com.google.gms.google-services'
```

<h4>1.3 - Em build.gradle (Project: AppName) , Adicione isso:</h4>

```cpp
dependencies {
    // Add this line
    classpath 'com.google.gms:google-services:4.3.2'
  }
```

<h3>2 - Implementação da SDK Inngage (inngage-lib) </h3>

<h4>2.1 - Em build.gradle (Module: app) implemente nossa dependências</h4>

```cpp
dependencies {      
  
       implementation 'com.github.inngage:inngage-lib:3.0.1-stable'

}
```

<h4>2.2 - Em build.gradle (Project: AppName) adicione o link do repositório</h4>

```cpp
dependencies {      
  
       buildscript{
  repositories{
              // adicione somente esta linha dentro de "repositories"
              maven { url 'https://jitpack.io' }
        
  }
}

allprojects {
      repositories {
              // adicione somente esta linha dentro deste outro "repositories"
              maven { url 'https://jitpack.io' }
      }
}

}
```


<h3>Adicionando InngageConstants Interface </h3>

<p>Visando desacoplar as constantes utilizadas em nossa integração, adicione uma nova interface ao seu projeto denominada "InngageConstants"</p>

<h4>3.1- Crie o arquivo como uma interface "InngageConstants" e adicione o seguinte código</h4>

```cpp
interface InngageConstants {

    String inngageAppToken = "APP_TOKEN"; //application token retirado da plataforma Inngage
    String inngageEnvironment = "prod";
    String googleMessageProvider = "FCM";

}
```

<h3>4 - Implementando Importações e Métodos na MainActivity  </h3>

<p>Na MainActivity existem algumas importações e métodos para se implementar para possibilitar o processo de :

subscription - Responsável por fazer a subscrição do usuário na plataforma Inngage;
notification handler - Responsável por fazer a manipulação da notificação quando recebida;</p>

<h4>4.1 - Começaremos importando:</h4>

```cpp
import br.com.inngage.sdk.InngageIntentService;
  import br.com.inngage.sdk.InngageUtils;import br.com.inngage.sdk.InngageIntentService;
  import br.com.inngage.sdk.InngageUtils;
```

<h4>4.2 - Logo após a abertura da classe que implementará esses métodos, adicione essas duas linhas:</h4>

```cpp
private static final String TAG = "INNGAGE";
```

<h4>4.3 - Adicione os seguintes métodos nesta mesma classe:</h4>

<p>
  <b>A - handleSubscription:</b> Esse método é responsável por fazer a subscrição do usuário na plataforma Inngage. Durante esse processo é possível definir um identificador amigável (identifier) ao usuário (CPF, e-mail, id do banco de dados, etc), e campos personalizados para compor a identidade do usuário de seu app (Nome, E-mail, Data de Nascimento, etc). Aprenda mais sobre campos personalizados clicando aqui.
</p>

<p>Faça a parametrização do "identificador" no método startInit a seu critério (email, username, uuid, id do usuário, cpf, ou mascare esse valor como preferir);</p>
<p>Caso o App não tenha campos personalizados, remova a primeira seção abaixo e o atributo do método startInit.</p>
<p>Certifique-se de definir os customFields na plataforma antes de usá-los aqui.</p>


```cpp
 private void handleSubscription() {
        
        String myInngageAppToken = InngageConstants.inngageAppToken;
        String myInngageEnvironment = InngageConstants.inngageEnvironment;
        String myGoogleMessageProvider = InngageConstants.googleMessageProvider;

        // caso o app possua campos customizáveis, descomente estas linhas, cheque-os na plataforma, substitua-os abaixo e adicione os valores de acordo com a sua aplicação
//        JSONObject jsonCustomField = new JSONObject();
//
//        try {
//
//            jsonCustomField.put("nome", user.name);
//            jsonCustomField.put("email", user.email);
//            jsonCustomField.put("telefone", "");
//            jsonCustomField.put("dataRegistro", "");
//            jsonCustomField.put("dataNascimento", "");
//
//
//      } catch (JSONException e) {
//
//            e.printStackTrace();
//      }
//        // caso possua campos customizáveis, adicione o "jsonCustomField" como na chamada abaixo
//        InngageIntentService.startInit(
//                this,
//                myInngageAppToken,
//                "Identifier", //Seu identificador
//                myInngageEnvironment,
//                myGoogleMessageProvider,
//    jsonCustomField);

        // caso não possua campos customizáveis até então:
        InngageIntentService.startInit(
                this,
                myInngageAppToken,
                "userIdentifier", //Seu identificador
                myInngageEnvironment,
                myGoogleMessageProvider);


        InngageUtils.handleNotification(this, getIntent(), myInngageAppToken, myInngageEnvironment);
```

<h3>5 - Chamando os métodosh</h3>

<p>Depois de adicionar os trechos de código acima, certifique-se de chamar os 3 métodos em onCreate ou onStart:</p>

```cpp
protected void onCreate(Bundle savedInstanceState) 
  {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        
       // adicione essa chamada ao onCreate ou onStart da sua activity
        handleSubscription();
      
      
  }
```

<h3>6 - Arquivo AndroidManifest.xml</h3>


<p>
  Depois de implementar a biblioteca, o arquivo de manifesto precisa de algumas permissões e serviços a serem adicionados.</p>


<h4>6.1 - Adicionando Permissões (antes da tag 'application' do AndroidManifest.xml) :</h4>

```cpp

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

<h4>6.2 - Antes de fechar a tag </ application>, adicione isto:</h4>

<p>
   Ícone de notificação !
é obrigatório colocar o ícone de notificação no repositório drawable sob o nome ic_notification como no exemplo abaixo:

android:resource="@drawable/ic_notification"
</p>

```cpp
<service android:name="br.com.inngage.sdk.PushMessagingService">
         <intent-filter>
         <action android:name="com.google.firebase.MESSAGING_EVENT"/>
         </intent-filter>
</service>

<meta-data
         android:name="com.google.firebase.messaging.default_notification_icon"
         android:resource="@drawable/ic_notification" />

<service android:name="br.com.inngage.sdk.InngageIntentService"
         android:exported="false">
         <intent-filter>
         <action android:name="com.google.firebase.INSTANCE_ID_EVENT"/>
         </intent-filter>
</service>
```

<h3>7 - Configuração abertura do Dialog</h3>

<p>
  Após finalizar toda implementação da SDK Inngage e demais dependências do Android, finalizaremos configurando na plataforma Inngage os parâmetros do seu aplicativo. Nesta sessão deveremos informar o nome do pacote (br.com.suaempresa.seuapp) e a classe que fará o recebimento do Push, no caso dessa implementação MainActivity.
</p>


<h2>Execute seu projeto e registre seu dispositivo</h2>

<p>
  O registro de um dispositivo de teste permitirá que você teste suas mensagens, alterações de variáveis em um dispositivo real.

Para registrar seu dispositivo, primeiro verifique se você está em ambiente de Desenvolvimento. Acesse o App gerado e abra a aba Todo Público em sua conta da Inngage, o Dispositivo deve estar registrado. Você pode olhar todas suas informações em variáveis clicando no ícone (Usuário) e enviar uma notificação clicando no ícone (Avião).

 Nunca use uma chave de desenvolvimento em um aplicativo em produção
O uso de uma chave de desenvolvimento pode não suportar usuários reais (em uma escala de produção). Além disso, todos os dados do usuário serão perdidos, pois não são capturados na análise.

</p>
## Contents
- [Requerimentos](#features)
- [Building](#building)
- [Using Edlib in your project](#using-edlib-in-your-project)
- [Usage and examples](#usage-and-examples)
- [API documentation](#api-documentation)
- [Alignment methods](#alignment-methods)
- [Aligner](#aligner)
- [Running tests](#running-tests)
- [Time and space complexity](#time-and-space-complexity)
- [Test data](#test-data)
- [Development and contributing](#development-and-contributing)
- [Publication](#publication)
- [Acknowledgements](#acknowledgements)

A lightweight and super fast C/C++ library for sequence alignment using [edit distance](https://en.wikipedia.org/wiki/Edit_distance).

Calculating edit distance of two strings is as simple as:
```c
edlibAlign("hello", 5, "world!", 6, edlibDefaultAlignConfig()).editDistance;
```

Edlib is also available for **Python** [![PyPI version](https://img.shields.io/pypi/v/edlib.svg) (Click here for Python README)](https://pypi.python.org/pypi/edlib), with code residing at [bindings/python](bindings/python).

There is also non-official [binding for Julia](https://github.com/cjdoris/Edlib.jl) by @cjdoris.

## Features
* Calculates **edit distance (Levenshtein distance)**.
* It can find **optimal alignment path** (instructions how to transform first sequence into the second sequence).
* It can find just the **start and/or end locations of alignment path** - can be useful when speed is more important than having exact alignment path.
* Supports **multiple [alignment methods](#alignment-methods)**: global(**NW**), prefix(**SHW**) and infix(**HW**), each of them useful for different scenarios.
* You can **extend character equality definition**, enabling you to e.g. have wildcard characters, to have case insensitive alignment or to work with degenerate nucleotides.
* It can easily handle small or **very large sequences**, even when finding alignment path, while consuming very little memory.
* **Super fast** thanks to Myers's bit-vector algorithm.





## Building
### Meson
Primary way of building Edlib is via [Meson](https://mesonbuild.com/) build tool.

Requirements: make sure that you have `meson` installed on your system.

Execute
```
make
```
to build **static** library and binaries (apps and tests) and also run tests.  
To build **shared** library and binaries, do `make LIBRARY_TYPE=shared`.

Library and binaries will be created in `meson-build` directory.  
You can choose alternate build directory like this: `make BUILD_DIR=some-other-dir`.

Optionally, you can run
```
sudo make install
```
to install edlib library on your machine (on Linux, this will usually install it to `usr/local/lib` and `usr/local/include`).

Check Makefile if you want to run individual steps on your own (building, tests, ...).

NOTE: If you need more control, use `meson` command directly, `Makefile` is here only to help with common commands.

### CMake
Edlib can alternatively be built with CMake.

Execute following command to build Edlib using CMAKE:
```
cd build && cmake -D CMAKE_BUILD_TYPE=Release .. && make
```
This will create binaries in `bin/` directory and libraries (static and shared) in `lib/` directory.

```
./bin/runTests
```
to run tests.

Optionally, you can run
```
sudo make install
```
to install edlib library on your machine.

### Conda
Edlib can also be installed via Conda: [![Anaconda-Server Badge](https://anaconda.org/bioconda/edlib/badges/installer/conda.svg)](https://conda.anaconda.org/bioconda): `conda install edlib`.


## Using Edlib in your project
You can use Edlib in you project by either directly copying header and source files from [edlib/](edlib/), or by linking Edlib library (see [Building](#building) for instructions how to build Edlib libraries).
In any case, only thing that you have to do in your source files is to include `edlib.h`.

To get you started quickly, let's take a look at a few ways to get simple Hello World project working.

Our Hello World project has just one source file, `helloWorld.cpp` file, and it looks like this:


Running it should output `edit_distance('hello', 'world!') = 5`.

### Approach #1: Directly copying edlib source and header files.
Here we directly copied [edlib/](edlib/) directory to our project, to get following project structure:
```
edlib/  -> copied from edlib/
  include/
    edlib.h
  src/
    edlib.cpp
helloWorld.cpp -> your program
```

Since `helloWorld` is a c++ program, we can compile it with just one line: `c++ helloWorld.cpp edlib/src/edlib.cpp -o helloWorld -I edlib/include`.

If hello world was a C program, we would compile it like this:
```
    c++ -c edlib/src/edlib.cpp -o edlib.o -I edlib/include
    cc -c helloWorld.c -o helloWorld.o -I edlib/include
    c++ helloWorld.o edlib.o -o helloWorld
```

### Approach #2: Copying edlib header file and static library.
Instead of copying edlib source files, you could copy static library (check [Building](#building) on how to create static library). We also need to copy edlib header files. We get following project structure:
```
edlib/  -> copied from edlib
  include/
    edlib.h
  edlib.a
helloWorld.cpp -> your program
```

Now you can compile it with `c++ helloWorld.cpp -o helloWorld -I edlib/include -L edlib -ledlib`.

### Approach #3: Install edlib library on machine.
Alternatively, you could avoid copying any Edlib files and instead install libraries by running `sudo make install` (check [Building](#building) for exact instructions depending on approach you used for building). Now, all you have to do to compile your project is `c++ helloWorld.cpp -o helloWorld -ledlib`.
If you get error message like `cannot open shared object file: No such file or directory`, make sure that your linker includes path where edlib was installed.

### Approach #4: Use edlib in your project via CMake.
If you are using CMake for compilation, we suggest adding edlib as a git submodule with the command `git submodule add https://github.com/martinsos/edlib vendor/edlib`. Afterwards, modify your top level CMakeLists.txt file accordingly:
```
add_subdirectory(vendor/edlib EXCLUDE_FROM_ALL)
target_link_libraries(your_exe edlib) # or target_link_libraries(your_exe edlib)
```
The `add_subdirectory` command adds a folder to the build tree, meaning it will run CMakeLists.txt from the included folder as well. Flag `EXCLUDE_FROM_ALL` disables building (and instalment) of targets in the added folder which are not needed in your project. In the above example only the (static) library `edlib` will be build, while `edlib-aligner`, `hello_world` and the rest won't. In order to access the `edlib` API, add `#include "edlib.h"` in your source file (CMake will automatically update your include path).


For more example projects take a look at applications in [apps/](apps/).


## Usage and examples
Main function in edlib is `edlibAlign`. Given two sequences (and their lengths), it will find edit distance, alignment path or its end and start locations.

```c
char* query = "ACCTCTG";
char* target = "ACTCTGAAA"
EdlibAlignResult result = edlibAlign(query, 7, target, 9, edlibDefaultAlignConfig());
if (result.status == EDLIB_STATUS_OK) {
    printf("%d", result.editDistance);
}
edlibFreeAlignResult(result);
```

NOTE: One character is expected to occupy one char/byte, meaning that characters spanning multiple chars/bytes are not supported. As long as your alphabet size is <= 256 you can manually map it to numbers/chars from 0 to 255 and solve this that way, but if its size is > 256 then you will not be able to use Edlib.

### Configuring edlibAlign()
`edlibAlign` takes configuration object (it is a struct `EdlibAlignConfig`), which allows you to further customize how alignment will be done. You can choose [alignment method](#alignment-methods), tell edlib what to calculate (just edit distance or also path and locations) and set upper limit for edit distance.

For example, if you want to use infix(HW) alignment method, want to find alignment path (and edit distance), are interested in result only if edit distance is not larger than 42 and do not want to extend character equality definition, you would call it like this:
```c
edlibAlign(seq1, seq1Length, seq2, seq2Length,
           edlibNewAlignConfig(42, EDLIB_MODE_HW, EDLIB_TASK_PATH, NULL, 0));
```
Or, if you want to use suffix(SHW) alignment method, want to find only edit distance, do not have any limits on edit distance and want character '?' to match both itself and characters 'X' and 'Y', you would call it like this:
```c
EdlibEqualityPair additionalEqualities[2] = {{'?', 'X'}, {'?', 'Y'}};
edlibAlign(seq1, seq1Length, seq2, seq2Length,
           edlibNewAlignConfig(-1, EDLIB_MODE_SHW, EDLIB_TASK_DISTANCE, additionalEqualities, 2));
```

We used `edlibNewAlignConfig` helper function to easily create config, however we could have also just created an instance of it and set its members accordingly.

### Handling result of edlibAlign()
`edlibAlign` function returns a result object (`EdlibAlignResult`), which will contain results of alignment (corresponding to the task that you passed in config).

```c
EdlibAlignResult result = edlibAlign(seq1, seq1Length, seq2, seq2Length,
                                     edlibNewAlignConfig(-1, EDLIB_MODE_HW, EDLIB_TASK_PATH, NULL, 0));
if (result.status == EDLIB_STATUS_OK) {
    printf("%d\n", result.editDistance);
    printf("%d\n", result.alignmentLength);
    printf("%d\n", result.endLocations[0]);
}
edlibFreeAlignResult(result);
```

It is important to remember to free the result object using `edlibFreeAlignResult` function, since Edlib allocates memory on heap for certain members. If you decide to do the cleaning manually and not use `edlibFreeAlignResult`, do not forget to manually `free()` required members.

### Turning alignment to cigar
Cigar is a standard way to represent alignment path.
Edlib has helper function that transforms alignment path into cigar.
```c
char* cigar = edlibAlignmentToCigar(result.alignment, result.alignmentLength, EDLIB_CIGAR_STANDARD);
printf("%s", cigar);
free(cigar);
```

## API documentation

For complete documentation of Edlib library API, visit [http://martinsos.github.io/edlib](https://martinsos.github.io/edlib) (should be updated to the latest release).

To generate the latest API documentation yourself from the source, you need to have [doxygen](www.doxygen.org) installed.
Position yourself in the root directory and run `doxygen`, this will generate `docs/` directory. Then open `docs/html/index.html` file with you favorite browser.

Alternatively, you can directly check [edlib.h](edlib/include/edlib.h).

## Alignment methods

Edlib supports 3 alignment methods:
* **global (NW)** - This is the standard method, when we say "edit distance" this is the method that is assumed.
  It tells us the smallest number of operations needed to transform first sequence into second sequence.
  *This method is appropriate when you want to find out how similar is first sequence to second sequence.*
* **prefix (SHW)** - Similar to global method, but with a small twist - gap at query end is not penalized. What that means is that deleting elements from the end of second sequence is "free"!
  For example, if we had `AACT` and `AACTGGC`, edit distance would be 0, because removing `GGC` from the end of second sequence is "free" and does not count into total edit distance.
  *This method is appropriate when you want to find out how well first sequence fits at the beginning of second sequence.*
* **infix (HW)**: Similar as prefix method, but with one more twist - gaps at query end **and start** are not penalized. What that means is that deleting elements from the start and end of second sequence is "free"!
  For example, if we had `ACT` and `CGACTGAC`, edit distance would be 0, because removing `CG` from the start and `GAC` from the end of second sequence is "free" and does not count into total edit distance.
  *This method is appropriate when you want to find out how well first sequence fits at any part of second sequence.* For example, if your second sequence was a long text and your first sequence was a sentence from that text, but slightly scrambled, you could use this method to discover how scrambled it is and where it fits in that text.
  *In bioinformatics, this method is appropriate for aligning read to a sequence.*


## Aligner
Edlib comes with a standalone aligner cli app, which can be found at [apps/aligner/](apps/aligner).

![Edlib aligner screenshot](images/edlib-aligner-screenshot.png)

Aligner reads sequences from fasta files, and it can display alignment path in graphical manner or as a cigar.
It also measures calculation time, so it can be useful for testing speed and comparing Edlib with other tools.

Check [Building](#building) to see how to build binaries (including `edlib-aligner`).
Run `./build/bin/edlib-aligner` with no params for help and detailed instructions.

Example of usage:
`./build/bin/edlib-aligner -p apps/aligner/test_data/query.fasta apps/aligner/test_data/target.fasta`

**NOTE**: Aligner currently does not work on Windows, because it uses `getopt` to parse command line arguments, which is not supported on Windows.


## Running tests
Check [Building](#building) to see how to build binaries (including binary `runTests`).
To run tests, just run `./runTests`. This will run random tests for each alignment method, and also some specific unit tests.


## Time and space complexity
Edlib is based on [Myers's bit-vector algorithm](http://www.gersteinlab.org/courses/452/09-spring/pdf/Myers.pdf) and extends from it.
It calculates a dynamic programming matrix of dimensions `Q x T`, where `Q` is the length of the first sequence (query), and `T` is the length of the second sequence (target). It uses Ukkonen's banded algorithm to reduce the space of search, and there is also parallelization from Myers's algorithm, however time complexity is still quadratic.
Edlib uses Hirschberg's algorithm to find alignment path, therefore space complexity is linear.

Time complexity: `O(T * Q)`.

Space complexity: `O(T + Q)`.

It is worth noting that Edlib works best for large, similar sequences, since such sequences get the highest speedup from banded approach and bit-vector parallelization.


## Test data
In [test_data/](test_data) directory there are different genome sequences, ranging from 10 kbp to 5 Mbp in length. They are ranging in length and similarity, so they can be useful for testing and measuring speed in different scenarios.


## Development and contributing
Feel free to send pull requests and raise issues.

When developing, you may want to use `-D CMAKE_BUILD_TYPE=Debug` flag when calling `cmake` in order to get debugging flags passed to compiler. This should also happen if you just run `cmake ..` with no flags, but I think I have noticed it does not always works as expected (probably has something to do with cmake cache). To check which flags is compiler using, run `make` with `VERBOSE=1`: `make VERBOSE=1`.


## Publication

Martin Šošić, Mile Šikić; Edlib: a C/C ++ library for fast, exact sequence alignment using edit distance. Bioinformatics 2017 btw753. doi: [10.1093/bioinformatics/btw753](https://doi.org/10.1093/bioinformatics/btw753)


## Acknowledgements

Mile Šikić (@msikic) - Mentoring and guidance through whole project.

Ivan Sović (@isovic) - Help with testing and prioritizing features, valuable comments on the manuscript.

## FAQ

### What do terms NW, HW and SHW stand for?
NW stands for Needleman-Wunsch, HW for Hybrid Wunsch, and SHW for Semi Hybrid Wunsch. While NW is a common abbreviation, HW and SHW abbreviations were made up at the very start of this project to describe additional modes of alignment. Later we started using terms "global", "infix" and "prefix" more, as they describe the modes better, but terms NW, HW and SHW are still very present in the project.
