package org.delcom.starter.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.delcom.starter.helper.AppBase64;
import java.util.Locale;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class HomeController {

    @GetMapping("/") /*  */
    public String hello() {
        return "Hay, selamat datang di aplikasi dengan Spring Boot!";
    }

    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable String name) {
        return "Hello, " + name + "!";
    }

    AppBase64 helper = new AppBase64();

    @GetMapping("/informasiNim/{nim}")
    public String informasiNim(@PathVariable String nim) {

        // Validasi panjang NIM
        if (nim.length() != 8) {
            return "NIM harus 8 karakter";
        }

        // Mengambil bagian-bagian NIM
        String prefix = nim.substring(0, 3);
        String angkatan = nim.substring(3, 5);
        String nomorUrut = nim.substring(5, 8);

        // Menentukan program studi
        String prodi;
        switch (prefix) {
            case "11S":
                prodi = "Sarjana Informatika";
                break;
            case "12S":
                prodi = "Sarjana Sistem Informasi";
                break;
            case "14S":
                prodi = "Sarjana Teknik Elektro";
                break;
            case "21S":
                prodi = "Sarjana Manajemen Rekayasa";
                break;
            case "22S":
                prodi = "Sarjana Teknik Metalurgi";
                break;
            case "31S":
                prodi = "Sarjana Teknik Bioproses";
                break;
            case "114":
                prodi = "Diploma 4 Teknologi Rekasaya Perangkat Lunak";
                break;
            case "113":
                prodi = "Diploma 3 Teknologi Informasi";
                break;
            case "133":
                prodi = "Diploma 3 Teknologi Komputer";
                break;
            default:
                return "Program Studi tidak Tersedia";
        }

        // Menentukan format urutan
        String urutan = nomorUrut.substring(2);

        // Gunakan format satu baris agar sesuai dengan test
        return "Inforamsi NIM " + nim + ": "
                + ">> Program Studi: " + prodi
                + ">> Angkatan: 20" + angkatan
                + ">> Urutan: " + urutan;

    }

    @GetMapping("/perolehanNilai/{strBase64}")
    public String perolehanNilai(@PathVariable String strBase64) throws IOException {

        String plain = helper.convertBase64ToPlain(strBase64, "base64.txt", "base64-ke-plain.txt");

        // Gunakan Scanner dengan Locale.US secara langsung agar nextDouble membaca
        // titik sebagai desimal
        Scanner sc = new Scanner(plain).useLocale(Locale.US);

        // Bobot komponen sesuai urutan input (asumsi bobot diberikan sebagai bilangan
        // bulat) }
        int bobotPA = sc.nextInt();
        int bobotT = sc.nextInt();
        int bobotK = sc.nextInt();
        int bobotP = sc.nextInt();
        int bobotUTS = sc.nextInt();
        int bobotUAS = sc.nextInt();
        int total = 0;
        total = bobotK + bobotP + bobotPA + bobotT + bobotUAS + bobotUTS;
        if (!(total == 100)) {
            return "Total bobot harus 100\n".replaceAll("\n", "<br/>");
        }
        sc.nextLine();

        // Simpan total skor & total maksimal per kategori sebagai double
        double totalPA = 0.0, maxPA = 0.0;
        double totalT = 0.0, maxT = 0.0;
        double totalK = 0.0, maxK = 0.0;
        double totalP = 0.0, maxP = 0.0;
        double totalUTS = 0.0, maxUTS = 0.0;
        double totalUAS = 0.0, maxUAS = 0.0;

        boolean adaError = false;

        // Baca input nilai sampai ketemu "---"
        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("---"))
                break;

            // Format yang diharapkan: SIMBOL|maksimal|nilai
            String[] parts = line.split("\\|");
            if (parts.length < 3) {
                adaError = true;
                continue; // Skip line ini, proses line berikutnya
            }

            String simbol = parts[0].trim();
            int maks;
            int nilai;

            maks = Integer.parseInt(parts[1].trim());
            nilai = Integer.parseInt(parts[2].trim());

            switch (simbol) {
                case "PA":
                    maxPA += maks;
                    totalPA += nilai;
                    break;
                case "T":
                    maxT += maks;
                    totalT += nilai;
                    break;
                case "K":
                    maxK += maks;
                    totalK += nilai;
                    break;
                case "P":
                    maxP += maks;
                    totalP += nilai;
                    break;
                case "UTS":
                    maxUTS += maks;
                    totalUTS += nilai;
                    break;
                case "UAS":
                    maxUAS += maks;
                    totalUAS += nilai;
                    break;
                default:
                    adaError = true;
                    break;
            }
        }

        // Hitung rata-rata (dalam persen)
        double rataPA = (maxPA == 0) ? 0.0 : (totalPA * 100.0 / maxPA);
        double rataT = (maxT == 0) ? 0.0 : (totalT * 100.0 / maxT);
        double rataK = (maxK == 0) ? 0.0 : (totalK * 100.0 / maxK);
        double rataP = (maxP == 0) ? 0.0 : (totalP * 100.0 / maxP);
        double rataUTS = (maxUTS == 0) ? 0.0 : (totalUTS * 100.0 / maxUTS);
        double rataUAS = (maxUAS == 0) ? 0.0 : (totalUAS * 100.0 / maxUAS);

        // Pembulatan ke bawah untuk persen
        int bulatPA = (int) Math.floor(rataPA);
        int bulatT = (int) Math.floor(rataT);
        int bulatK = (int) Math.floor(rataK);
        int bulatP = (int) Math.floor(rataP);
        int bulatUTS = (int) Math.floor(rataUTS);
        int bulatUAS = (int) Math.floor(rataUAS);

        // Kontribusi nilai akhir
        double nilaiPA = (bulatPA / 100.0) * bobotPA;
        double nilaiT = (bulatT / 100.0) * bobotT;
        double nilaiK = (bulatK / 100.0) * bobotK;
        double nilaiP = (bulatP / 100.0) * bobotP;
        double nilaiUTS = (bulatUTS / 100.0) * bobotUTS;
        double nilaiUAS = (bulatUAS / 100.0) * bobotUAS;

        double totalNilai = nilaiPA + nilaiT + nilaiK + nilaiP + nilaiUTS + nilaiUAS;

        // Tutup scanner
        sc.close();
        // seperti requirement test.
        plain = "";
        if (adaError) {
            plain = "Data tidak valid. Silahkan menggunakan format: Simbol|Bobot|Perolehan-Nilai\n"
                    + "Simbol tidak dikenal\n";
        }
        plain = plain + "Perolehan Nilai:\n"
                + ">> Partisipatif: " + bulatPA + "/100 (" + String.format(Locale.US, "%.2f", nilaiPA) + "/"
                + bobotPA + ")\n"
                + ">> Tugas: " + bulatT + "/100 (" + String.format(Locale.US, "%.2f", nilaiT) + "/" + bobotT
                + ")\n"
                + ">> Kuis: " + bulatK + "/100 (" + String.format(Locale.US, "%.2f", nilaiK) + "/" + bobotK
                + ")\n"
                + ">> Proyek: " + bulatP + "/100 (" + String.format(Locale.US, "%.2f", nilaiP) + "/" + bobotP
                + ")\n"
                + ">> UTS: " + bulatUTS + "/100 (" + String.format(Locale.US, "%.2f", nilaiUTS) + "/" + bobotUTS
                + ")\n"
                + ">> UAS: " + bulatUAS + "/100 (" + String.format(Locale.US, "%.2f", nilaiUAS) + "/" + bobotUAS
                + ")\n\n"
                + ">> Nilai Akhir: " + String.format(Locale.US, "%.2f", totalNilai) + "\n"
                + ">> Grade: " + helper.getGrade(totalNilai) + "\n";
        plain = plain.replaceAll("\n", "<br/>").trim();
        helper.convertPlainToBase64(plain, "plain.txt",
                "plain-ke-base64.txt");
        return plain;

    }

    @GetMapping("/perbedaanL/{strBase64}")
    public String perbedaanL(@PathVariable String strBase64) throws IOException {

        String plain = helper.convertBase64ToPlain(strBase64, "base64.txt", "base64-ke-plain.txt");
        Scanner sc = new Scanner(plain);

        // Input ukuran matriks
        int n = sc.nextInt();

        // Membuat matriks
        int[][] matrix = new int[n][n];

        // Input elemen matriks
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = sc.nextInt();
            }
        }

        // Menghitung nilai L, kebalikan L, dan tengah
        int nilaiL = 0;
        int nilaiKebalikanL = 0;
        int nilaiTengah = 0;

        // Jika matriks 1x1
        if (n == 1) {
            nilaiTengah = matrix[0][0];
            sc.close();
            plain = "Nilai L: Tidak Ada\n"
                    + "Nilai Kebalikan L: Tidak Ada\n"
                    + "Nilai Tengah: " + nilaiTengah + "\n"
                    + "Perbedaan: Tidak Ada\n"
                    + "Dominan: " + nilaiTengah + "\n";
            plain = plain.replaceAll("\n", "<br/>");
            helper.convertPlainToBase64(plain, "plain.txt", "plain-ke-base64.txt");
            return plain;
        }

        // Jika matriks 2x2
        if (n == 2) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    nilaiTengah += matrix[i][j];
                }
            }
            sc.close();
            plain = "Nilai L: Tidak Ada\n"
                    + "Nilai Kebalikan L: Tidak Ada\n"
                    + "Nilai Tengah: " + nilaiTengah + "\n"
                    + "Perbedaan: Tidak Ada\n"
                    + "Dominan: " + nilaiTengah + "\n";
            plain = plain.replaceAll("\n", "<br/>");
            helper.convertPlainToBase64(plain, "plain.txt", "plain-ke-base64.txt");
            return plain;
        }

        // Menghitung nilai L (kolom pertama + baris terakhir dari j=1 hingga n-2)
        for (int i = 0; i < n; i++) {
            nilaiL += matrix[i][0];
        }
        for (int j = 1; j < n - 1; j++) {
            nilaiL += matrix[n - 1][j];
        }
        // Menghitung nilai kebalikan L (baris pertama dari j=1 hingga n-1 + kolom
        // terakhir dari i=1 hingga n-1)
        for (int j = 1; j < n; j++) {
            nilaiKebalikanL += matrix[0][j];
        }
        for (int i = 1; i < n; i++) {
            nilaiKebalikanL += matrix[i][n - 1];
        }

        // Menghitung nilai tengah (hanya untuk matriks n >= 3)
        if (n % 2 == 1) {
            nilaiTengah = matrix[n / 2][n / 2];
        } else {
            int a = n / 2 - 1;
            int b = n / 2;
            nilaiTengah = matrix[a][a] + matrix[a][b] + matrix[b][a] + matrix[b][b];
        }

        // Menghitung perbedaan
        int perbedaan = Math.abs(nilaiL - nilaiKebalikanL);

        // Menentukan dominan
        int dominan = (nilaiL == nilaiKebalikanL) ? nilaiTengah : Math.max(nilaiL, nilaiKebalikanL);

        // Output hasil
        sc.close();

        plain = "Nilai L: " + nilaiL + "\n"
                + "Nilai Kebalikan L: " + nilaiKebalikanL + "\n"
                + "Nilai Tengah: " + nilaiTengah + "\n"
                + "Perbedaan: " + perbedaan + "\n"
                + "Dominan: " + dominan + "\n";
        plain = plain.replaceAll("\n", "<br/>");
        helper.convertPlainToBase64(plain, "plain.txt", "plain-ke-base64.txt");
        return plain;
    }

    // @GetMapping("/palingTer/{strBase64}")
    // public String palingTer(@PathVariable String strBase64) throws IOException {
    // String plain = helper.convertBase64ToPlain(strBase64, "base64.txt",
    // "base64-ke-plain.txt");

    // // Daftar untuk menyimpan semua angka yang dimasukkan secara berurutan.
    // ArrayList<Integer> semuaAngka = new ArrayList<>();

    // // --- Proses Input ---
    // // Membaca input dari pengguna baris per baris sampai menemukan "---".
    // String[] lines = plain.split("\n");

    // if (lines[0].trim().equals("---")) {
    // plain = "Informasi tidak tersedia";
    // helper.convertPlainToBase64(plain, "plain.txt", "plain-ke-base64.txt");
    // return plain;
    // }

    // for (String input : lines) {
    // input = input.trim();

    // // Jika menemukan tanda "---", keluar dari loop
    // if (input.equals("---")) {
    // break;
    // }

    // // Konversi String ke int
    // int angka = Integer.parseInt(input);
    // semuaAngka.add(angka);
    // }

    // // --- Proses Kalkulasi ---
    // // Inisialisasi variabel untuk menyimpan hasil analisis.
    // int angkaMax = Integer.MIN_VALUE;
    // int angkaMin = Integer.MAX_VALUE;

    // int modus = 0; // Angka yang paling sering muncul
    // int frekuensiModus = 0; // Frekuensi dari modus

    // int antiModus = 0; // Angka yang paling jarang muncul
    // int frekuensiAntiModus = Integer.MAX_VALUE; // Frekuensi dari anti-modus

    // int angkaJumlahMax = 0; // Angka yang menghasilkan total (angka * frekuensi)
    // terbesar
    // int totalMax = 0; // Nilai total terbesar

    // int angkaJumlahMin = 0; // Angka yang menghasilkan total (angka * frekuensi)
    // terkecil
    // int totalMin = Integer.MAX_VALUE; // Nilai total terkecil

    // // ArrayList untuk menyimpan angka unik yang sudah diproses
    // ArrayList<Integer> angkaUnik = new ArrayList<>();

    // // Melakukan iterasi pada semua angka
    // for (int angka : semuaAngka) {
    // // Skip jika angka sudah diproses sebelumnya
    // if (angkaUnik.contains(angka)) {
    // continue;
    // }

    // // Tandai angka ini sudah diproses
    // angkaUnik.add(angka);

    // // Hitung frekuensi kemunculan angka ini
    // int jumlahKemunculan = 0;
    // for (int a : semuaAngka) {
    // if (a == angka) {
    // jumlahKemunculan++;
    // }
    // }

    // int total = angka * jumlahKemunculan;

    // // Mencari angka tertinggi (max) dan terendah (min).
    // if (angka > angkaMax) {
    // angkaMax = angka;
    // }
    // if (angka < angkaMin) {
    // angkaMin = angka;
    // }

    // // Modus → jika frekuensi lebih besar, atau sama (akan mengambil yang
    // terakhir
    // // ditemukan)
    // if (jumlahKemunculan > frekuensiModus ||
    // (jumlahKemunculan == frekuensiModus && angka > modus)) {
    // modus = angka;
    // frekuensiModus = jumlahKemunculan;
    // }

    // // Anti-modus → jika frekuensi lebih kecil, atau sama (akan mengambil yang
    // // terakhir ditemukan)
    // if (jumlahKemunculan < frekuensiAntiModus) {
    // antiModus = angka;
    // frekuensiAntiModus = jumlahKemunculan;
    // }

    // // Mencari jumlah total terbesar.
    // if (total > totalMax || (total == totalMax && angka > angkaJumlahMax)) {
    // angkaJumlahMax = angka;
    // totalMax = total;
    // }

    // // Mencari jumlah total terkecil.
    // if (total < totalMin) {
    // totalMin = total;
    // angkaJumlahMin = angka;
    // }
    // }

    // // Hitung frekuensi untuk angkaJumlahMax dan angkaJumlahMin (untuk output)
    // int frekJumlahMax = 0;
    // int frekJumlahMin = 0;
    // for (int a : semuaAngka) {
    // if (a == angkaJumlahMax)
    // frekJumlahMax++;
    // if (a == angkaJumlahMin)
    // frekJumlahMin++;
    // }

    // // --- Cetak Hasil ---
    // plain = "Tertinggi: " + angkaMax + "\n"
    // + "Terendah: " + angkaMin + "\n"
    // + "Terbanyak: " + modus + " (" + frekuensiModus + "x)\n"
    // + "Tersedikit: " + antiModus + " (" + frekuensiAntiModus + "x)\n"
    // + "Jumlah Tertinggi: " + angkaJumlahMax + " * " + frekJumlahMax + " = " +
    // totalMax + "\n"
    // + "Jumlah Terendah: " + angkaJumlahMin + " * " + frekJumlahMin + " = " +
    // totalMin + "\n";

    // plain = plain.replaceAll("\n", "<br/>");
    // helper.convertPlainToBase64(plain, "plain.txt", "plain-ke-base64.txt");
    // return plain;
    // }

    // }

    @GetMapping("/palingTer/{strBase64}")
    public String palingTer(@PathVariable String strBase64) throws IOException {
        String decodedInput = helper.convertBase64ToPlain(strBase64, "base64.txt", "base64-ke-plain.txt");

        String[] lines = decodedInput.split("\\R");
        HashMap<Integer, Integer> hashMapCounter = new HashMap<>();
        ArrayList<Integer> daftarNilai = new ArrayList<>();
        HashMap<Integer, Integer> hashMapTotal = new HashMap<>();
        if (lines[0].equals("---")) {
            return "Informasi tidak tersedia";
        }
        for (int i = 0; i < lines.length - 1; i++) {

            int nilai = Integer.parseInt(lines[i]);
            daftarNilai.add(nilai);

            // Menyimpan frekuensi kemunculan
            hashMapCounter.put(nilai, hashMapCounter.getOrDefault(nilai, 0) + 1);

        }

        // Inisialisasi nilai awal
        int nilaiTertinggi = 0;
        int nilaiTerendah = 1000;

        for (int nilai : daftarNilai) {

            // Total untuk setiap nilai
            int totalSekarang = hashMapTotal.getOrDefault(nilai, 0) + nilai;
            hashMapTotal.put(nilai, totalSekarang);

            if (nilai > nilaiTertinggi) {
                nilaiTertinggi = nilai;
            } else {
                continue;
            }
        }

        for (int nilai : daftarNilai) {
            if (nilai < nilaiTerendah) {
                nilaiTerendah = nilai;
            } else {
                continue;
            }
        }

        int[] arrayNilai = daftarNilai.stream().mapToInt(Integer::intValue).toArray();

        int nilaiJumlahTertinggi = 0;
        int nilaiJumlahTerendah = arrayNilai[0];
        int frekuensiJumlahTertinggi = 0;
        int jumlahTertinggi = 0;
        int jumlahTerendah = 0;
        jumlahTertinggi = java.util.Collections.max(hashMapTotal.values());
        jumlahTerendah = hashMapTotal.get(nilaiJumlahTerendah);

        for (HashMap.Entry<Integer, Integer> entry : hashMapTotal.entrySet()) {
            int nilai = entry.getKey();
            int total = entry.getValue();
            if (total == jumlahTertinggi) {
                nilaiJumlahTertinggi = nilai;
                frekuensiJumlahTertinggi = hashMapCounter.get(nilai);
            }
            if (jumlahTerendah > total) {
                nilaiJumlahTerendah = nilai;
                jumlahTerendah = total;
            } else {
                continue;
            }
        }

        HashMap<Integer, Integer> hashMapCounterTerbanyak = new HashMap<>();
        int nilaiTerbanyak = arrayNilai[0];
        int frekuensiTerbanyak = 0;
        for (int i = 0; i < arrayNilai.length; i++) {
            hashMapCounterTerbanyak.put(arrayNilai[i], hashMapCounterTerbanyak.getOrDefault(arrayNilai[i], 0) + 1);
            int frekuensiSaatIni = hashMapCounterTerbanyak.get(arrayNilai[i]);
            if (frekuensiSaatIni > frekuensiTerbanyak) {
                nilaiTerbanyak = arrayNilai[i];
                frekuensiTerbanyak = frekuensiSaatIni;
            }
        }

        int nilaiTersedikit = arrayNilai[0];
        HashMap<Integer, Integer> hashMapCounterTersedikit = new HashMap<>();
        int frekuensiTersedikit = 0;
        hashMapCounterTersedikit.put(nilaiTersedikit, 1);
        for (int i = 1; i < arrayNilai.length; i++) {
            hashMapCounterTersedikit.put(arrayNilai[i], hashMapCounterTersedikit.getOrDefault(arrayNilai[i], 0) + 1);
            if (arrayNilai[i] != nilaiTersedikit) {
                continue;
            } else {
                boolean foundNewValue = false;
                for (int j = i + 1; j < arrayNilai.length; j++) {
                    if (foundNewValue) {
                        continue;
                    }

                    if (!hashMapCounterTersedikit.containsKey(arrayNilai[j])) {
                        hashMapCounterTersedikit.put(arrayNilai[j], 1);
                        nilaiTersedikit = arrayNilai[j];
                        frekuensiTersedikit = hashMapCounter.get(nilaiTersedikit);
                        i = j;
                        foundNewValue = true;
                    } else {
                        continue;
                    }
                }
            }
        }

        String output = "";
        output += "Tertinggi: " + nilaiTertinggi + "\n";
        output += "Terendah: " + nilaiTerendah + "\n";
        output += "Terbanyak: " + nilaiTerbanyak + " " + "(" + frekuensiTerbanyak + "x)" + "\n";
        output += "Tersedikit: " + nilaiTersedikit + " " + "(" + frekuensiTersedikit + "x)" + "\n";
        output += "Jumlah Tertinggi: " + nilaiJumlahTertinggi + " * " + frekuensiJumlahTertinggi + " = "
                + jumlahTertinggi + "\n";
        output += "Jumlah Terendah: " + nilaiJumlahTerendah + " * " + hashMapCounter.get(nilaiJumlahTerendah) + " = "
                + jumlahTerendah + "\n";

        output = output.replaceAll("\n", "<br/>").trim();
        helper.convertPlainToBase64(output, "plain.txt", "plain-ke-base64.txt");
        return output;
    }
}
