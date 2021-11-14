1)EclipseProjesi adlı klasörün içinde eclipse üzerinde çalışan kaynak kodları bulunmaktadır.

2)Javadoc adlı klasörün içinde projenin javadoc .html dosyaları vardır. allclasses.html'e girerek görülebilir.

3)KaynakKodu klasörünün içinde terminalden çalışmaya uygun hazırlanmış bir kod vardır. JavaFX'in lib dizini yer aldığı için sadece Javanın yüklü olması yeterlidir. Rapordaki komutlarla çalıştırılabilir(Gerekli library'ler KaynakKodu/lib/ klasörü içine koyulmalıdır).Font nedeniyle çalışmama ihtimaline karşın komutlar şu şekildedir:

Compile için : javac -cp lib/*:. Main.java
Run için     : java --module-path lib --add-modules javafx.controls,javafx.fxml -cp .:* Main


4)Rapor klasörünün için projenin raporu ve raporun latex kaynak kodu vardır. Birçok bilgi burada bulunabilir.

5)YüklemeDokümanı klasörü için proje için gerekli olan programların yüklenme adımları yer almaktadır.Gerekli programlar yoksa burdan bakılarak yüklenebilir.

Not :Raporun sonlarında projenin çalışırkenki ekran görüntüleri ve videosunun linki bulunmaktadır.
Not2:Rapor veya yükleme dokümanı PDF'inin yazı formatı nedeniyle kopyala yapıştırla komutlar çalışmayabilir. Elle yazmayı denerseniz veya kaynak kodundan alırsanız çalışacaktır. ("--" li kısımlar tek bir uzun çizgi olarak göstermiş durum bundan kaynaklı)

Fatih Selim YAKAR
