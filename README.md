### Hi there 👋

Pada repository ini saya membuat aplikasi dengan tema **Story App** yan saya beri judul **StoryAppMVVM**

Aplikasi ini menampilkan daftar cerita dari teman-teman yang sedang belajar dan lulus dari Dicoding

Pada Aplikasi ini memiliki fitur:

1. Authentifikasi:
    * Menampilkan halaman login untuk masuk ke dalam aplikasi
    * Membuat halaman register untuk mendaftarkan diri dalam aplikasi
    * Membuat Custom View berupa EditText pada halaman login atau register
    * Menyimpan data sesi dan token di preferences
    
2. List Story: 
    * Menampilkan daftar cerita dari API 
    * Muncul halaman detail ketika salah satu item cerita ditekan.

3. Tambah Story:
    * Halaman untuk menambah cerita baru yang dapat diakses dari halaman daftar cerita

4. Animasi: 
    * Membuat animasi pada aplikasi dengan menggunakan property Animation

5. Localization :
    * Terdapat pengaturan untuk localization (multi bahasa)

6. Clean Arsitecture :
    * Menerapkan MVVM untuk memisahkan antar layer
    
7. Kamera:
    * Menerapkan IntentCameraX untuk mengambilan gambar
    * Menerapkan IntentGallery untuk mengambil gambar dari gallery

8. Maps:
    * Menampilkan Maps berisi peta yang menampilkan semua cerita yang memiliki lokasi dengan benar.
    * Menerapkan Custom MapStyle
    * Menambahkan input lokasi saat ini dari GPS (menggunakan map) ketika tambah cerita.
 
9. Paging List:
    * Menampilkan List menggunakan Paging 3 dan Remote Mediator yang mengambil data dari RemoteDataSource dan Local
    
10. Testing:
    * Menerapkan unit test pada fungsi di dalam ViewModel yang mengambil list data Paging.
    * Menerapkan UI test dan idling resources untuk Memastikan mekanisme proses tambah cerita
    
  
