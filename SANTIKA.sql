CREATE TYPE "agama" AS ENUM (
  'ISLAM',
  'KATOLIK',
  'PROTESTAN',
  'HINDU',
  'BUDHA',
  'KONGHUCU'
);

CREATE TYPE "status_perkawinan" AS ENUM (
  'BELUM KAWIN',
  'KAWIN',
  'KAWIN BELUM TERCATAT',
  'CERAI HIDUP',
  'CERAI MATI'
);

CREATE TYPE "pendidikan" AS ENUM (
  'TIDAK SEKOLAH',
  'TK',
  'SD',
  'SMP',
  'SMA',
  'SLTA/SEDERAJAT',
  'D1',
  'D2',
  'D3',
  'D4',
  'S1',
  'S2',
  'S3'
);

CREATE TYPE "hubungan" AS ENUM (
  'AYAH',
  'IBU',
  'ISTRI',
  'SUAMI',
  'SAUDARA',
  'ANAK',
  'DIRI SENDIRI',
  'LAIN-LAIN',
  'TEMAN'
);

CREATE TYPE "status_pegawai" AS ENUM (
  'TETAP',
  'KONTRAK',
  'TRAINING',
  'TENAGA LUAR',
  'MAGANG',
  'RESIGN',
  'STAFF LUAR'
);

CREATE TABLE "pasien" (
  "id" uuid,
  "no_rekam_medis" varchar(15),
  "nama_pasien" varchar(50),
  "jenis_identitas" "enum(NIK,PASSPORT,NPWP,LAIN)",
  "identitas_lain" varchar(30),
  "no_identitas" varchar(50),
  "kelamin" "enum(L,P)",
  "tempat_lahir" varchar(30),
  "tanggal_lahir" date,
  "nama_ibu" varchar(20),
  "gol_darah" "enum(A,B,AB,O)",
  "pekerjaan" varchar(50),
  "perkawinan" status_perkawinan DEFAULT 'BELUM KAWIN',
  "agama" agama DEFAULT 'ISLAM',
  "telephone" varchar(20),
  "email" varchar(20),
  "pendidikan" pendidikan DEFAULT 'TIDAK SEKOLAH',
  "tanggal_daftar" date,
  "perusahaan_pasien_id" uuid,
  "suku_bangsa_id" uuid,
  "bahasa_pasien_id" uuid,
  "cacat_fisik_id" uuid,
  "provinsi_id" uuid,
  "kabupaten_id" uuid,
  "kecamatan_id" uuid,
  "kelurahan_id" uuid,
  "alamat" varchar(200)
);

CREATE TABLE "perusahaan_pasien" (
  "id" uuid,
  "nama_perusahaan" varchar(50),
  "alamat_perusahaan" varchar(150),
  "telephone_perusahaan" varchar(30)
);

CREATE TABLE "bahasa_pasien" (
  "id" uuid,
  "nama_bahasa" varchar(50)
);

CREATE TABLE "suku_bangsa_pasien" (
  "id" uuid,
  "nama_suku_bangsa" varchar(50)
);

CREATE TABLE "cacat_fisik_pasien" (
  "id" uuid,
  "nama_cacat" varchar(50)
);

CREATE TABLE "provinsi" (
  "id" uuid,
  "kode_provinsi" varchar(20),
  "nama_provinsi" varchar(50)
);

CREATE TABLE "kab_kota" (
  "id" uuid,
  "provinsi_id" uuid,
  "kode_kab_kota" varchar(20),
  "nama_kab_kota" varchar(50)
);

CREATE TABLE "kecamatan" (
  "id" uuid,
  "kab_kota_id" uuid,
  "kode_kecamatan" varchar(20),
  "nama_kecamatan" varchar(50)
);

CREATE TABLE "kelurahan" (
  "id" uuid,
  "kecamatan_id" uui,
  "kode_kelurahan" varchar(20),
  "nama_kelurahan" varchar(50)
);

CREATE TABLE "penjamin" (
  "id" uuid,
  "penjamin" varchar(30),
  "nama_perusahaan_penjamin" varchar(30),
  "alamat_penjamin" varchar(200),
  "telephone_penjamin" varchar(20),
  "penerima_tagihan" varchar(30),
  "is_active" bool DEFAULT true
);

CREATE TABLE "penjamin_pasien" (
  "id" uuid,
  "pasien_id" uuid,
  "penjamin_id" uuid,
  "no_peserta" varchar(20)
);

CREATE TABLE "penangung_jawab_pasien" (
  "id" uuid,
  "pasien_id" uuid,
  "hubungan" hubungan,
  "nama_penangung_jawab" varchar(30),
  "telephone_penangung_jawab" varchar(20),
  "pekerjaan_penangung_jawab" varchar(30),
  "alamat_penangung_jawan" varchar(200)
);

CREATE TABLE "pegawai" (
  "id" uuid,
  "nama" varchar(50),
  "nip" varchar(50) UNIQUE,
  "jenis_identitas" "enum(NIK,PASSPORT,NPWP,LAIN)",
  "identitas_lain" varchar(30),
  "no_identitas" varchar(50),
  "kelamin" "enum(L,P)",
  "jabatan" varchar(50),
  "telephone" varchar(20),
  "email" varchar(50),
  "jenjang_jabatan" varchar(20),
  "kelompok_pegawai_id" uuid,
  "resiko_pegawai_id" uuid,
  "darurat_pegawai_id" uuid,
  "departemen_pegawai_id" uuid,
  "bidang_pegawai_id" uuid,
  "status_pegawai" status_pegawai DEFAULT 'KONTRAK',
  "vendor_tenaga_luar_id" uuid,
  "pendidikan" varchar(20),
  "tempat_lahir" varchar(50),
  "tanggal_lahir" date,
  "provinsi_id" uuid,
  "kabupaten_id" uuid,
  "kecamatan_id" uuid,
  "kelurahan_id" uuid,
  "alamat" varchar(200),
  "mulai_bekerja" date,
  "foto" uuid,
  "is_active" bool DEFAULT true
);

CREATE TABLE "kelompok_pegawai" (
  "id" uuid,
  "kode_kelompok" varchar(50) UNIQUE,
  "nama_kelompok" varchar(50)
);

CREATE TABLE "resiko_pegawai" (
  "id" uuid,
  "kode_resiko" varchar(50),
  "nama_resiko" varchar(50)
);

CREATE TABLE "darurat_pegawai" (
  "id" uuid,
  "kode_emergency" varchar(50),
  "nama_emergency" varchar(50)
);

CREATE TABLE "departemen_pegawai" (
  "id" uuid,
  "kode_departemen" varchar(50),
  "nama_departemen" varchar(50)
);

CREATE TABLE "bidang_pegawai" (
  "id" uuid,
  "kode_bidang" varchar(50),
  "nama_bidang" varchar(50)
);

CREATE TABLE "vendor_tenaga_luar" (
  "id" uuid,
  "nama_vendor" varchar(100),
  "alamat_vendor" varchar(200),
  "bidang_vendor" varchar(100),
  "telephone_vendor" varchar(20),
  "nama_kontak_darurat" varchar(20),
  "kontak_darurat_vendor" varchar(20)
);

CREATE TABLE "dokumen_pegawai" (
  "id" uuid,
  "pegawai_id" uuid,
  "nama_dokumen" varchar(100),
  "dokumen" uuid,
  "is_active" bool DEFAULT true
);

CREATE TABLE "file" (
  "id" uuid,
  "nama_asli_file" varchar(255),
  "masuk_tanggal" datetime,
  "ext" varchar(20),
  "size" varchar(30),
  "status" enum(private,public),
  "file" varchar(255)
);

CREATE TABLE "dokter" (
  "id" uuid,
  "pegawai_id" uuid,
  "nama_dokter" varchar(50),
  "nip" varchar(50),
  "telephone_dokter" varchar(50),
  "email_dokter" varchar(50),
  "spesialis" uuid,
  "alumni" varchar(50),
  "no_ijin_praktek" varchar(120),
  "document_ijin_prakterk" uuid,
  "is_active" bool DEFAULT true
);

CREATE TABLE "spesialis" (
  "id" uuid,
  "kode_spesialis" varchar(50),
  "nama_spesialis" varchar(50)
);

CREATE TABLE "poli" (
  "id" uuid,
  "kode_poli" varchar,
  "nama_poli" varchar
);

CREATE TABLE "registrasi_pasien" (
  "id" uuid,
  "pasien_id" uuid,
  "no_rawat" varchar UNIQUE,
  "tanggal_registrasi" datetime,
  "tanggal_periksa" datetime,
  "dokter_id" uuid,
  "poli_id" uuid,
  "penjamin_id" uuid,
  "biaya_reg" double,
  "status" "enum(Belum,Sudah,Batal,Dirujuk,etc)",
  "status_daftar" "enum(BARU,LAMA)",
  "umur_daftar" varchar,
  "perawatan" "enum(RAWAT INAP,RAWAT JALAN)",
  "status_bayar" "enum(SUDAH,BELUM)",
  "status_poli" "enum(BARU,LAMA)"
);

CREATE TABLE "diagnosa_icd_10" (
  "id" uuid,
  "kode_diagnosa" varchar(50),
  "nama_diagnosa" varchar(255)
);

CREATE TABLE "prosedur_icd_9" (
  "id" uuid,
  "kode_procedur" varchar(50),
  "nama_procedur" varchar(255)
);

CREATE TABLE "episode_rawat_jalan" (
  "id" uuid,
  "registrasi_pasien_id" uuid,
  "keluhan_utama" text,
  "anamnesa" text,
  "pemeriksaan_fisik" text,
  "tekanan_darah" varchar(20),
  "nadi" varchar(10),
  "suhu" varchar(10),
  "berat_badan" varchar(10),
  "tinggi_badan" varchar(10),
  "resep" text,
  "catatan_dokter" text
);

CREATE TABLE "diagnosa_rawat_jalan" (
  "id" uuid,
  "episode_rawat_jalan_id" uuid,
  "diagnosa_id" uuid,
  "prioritas" integer,
  "jumlah" integer
);

CREATE TABLE "prosedur_rawat_jalan" (
  "id" uuid,
  "episode_rawat_jalan_id" uuid,
  "prosedur_id" uuid,
  "prioritas" integer,
  "jumlah" integer
);

CREATE TABLE "antrian_poli" (
  "id" uuid,
  "registrasi_pasien_id" uuid,
  "nama_pasien" string,
  "tanggal_periksa" datetime,
  "kode_poli" string,
  "nama_poli" string,
  "nama_dokter" string,
  "nomer_antrian" string,
  "panggilan" "enum(SUDAH,BELUM)",
  "pengulangan" integer
);

CREATE TABLE "task_id" (
  "id" uuid,
  "registrasi_pasien_id" uuid,
  "task_1" datetime,
  "task_2" datetime,
  "task_3" datetime,
  "task_4" datetime,
  "task_5" datetime,
  "task_6" datetime,
  "task_7" datetime
);

ALTER TABLE "pasien" ADD FOREIGN KEY ("perusahaan_pasien_id") REFERENCES "perusahaan_pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pasien" ADD FOREIGN KEY ("suku_bangsa_id") REFERENCES "suku_bangsa_pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pasien" ADD FOREIGN KEY ("bahasa_pasien_id") REFERENCES "bahasa_pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pasien" ADD FOREIGN KEY ("cacat_fisik_id") REFERENCES "cacat_fisik_pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pasien" ADD FOREIGN KEY ("provinsi_id") REFERENCES "provinsi" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pasien" ADD FOREIGN KEY ("kabupaten_id") REFERENCES "kab_kota" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pasien" ADD FOREIGN KEY ("kecamatan_id") REFERENCES "kecamatan" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pasien" ADD FOREIGN KEY ("kelurahan_id") REFERENCES "kelurahan" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "kab_kota" ADD FOREIGN KEY ("provinsi_id") REFERENCES "provinsi" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "kecamatan" ADD FOREIGN KEY ("kab_kota_id") REFERENCES "kab_kota" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "kelurahan" ADD FOREIGN KEY ("kecamatan_id") REFERENCES "kecamatan" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "penjamin_pasien" ADD FOREIGN KEY ("pasien_id") REFERENCES "pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "penjamin_pasien" ADD FOREIGN KEY ("penjamin_id") REFERENCES "penjamin" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "penangung_jawab_pasien" ADD FOREIGN KEY ("pasien_id") REFERENCES "pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("kelompok_pegawai_id") REFERENCES "kelompok_pegawai" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("resiko_pegawai_id") REFERENCES "resiko_pegawai" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("darurat_pegawai_id") REFERENCES "darurat_pegawai" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("departemen_pegawai_id") REFERENCES "departemen_pegawai" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("bidang_pegawai_id") REFERENCES "bidang_pegawai" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("vendor_tenaga_luar_id") REFERENCES "vendor_tenaga_luar" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("provinsi_id") REFERENCES "provinsi" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("kabupaten_id") REFERENCES "kab_kota" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("kecamatan_id") REFERENCES "kecamatan" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("kelurahan_id") REFERENCES "kelurahan" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pegawai" ADD FOREIGN KEY ("foto") REFERENCES "file" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "dokumen_pegawai" ADD FOREIGN KEY ("pegawai_id") REFERENCES "pegawai" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "dokumen_pegawai" ADD FOREIGN KEY ("dokumen") REFERENCES "file" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "dokter" ADD FOREIGN KEY ("pegawai_id") REFERENCES "pegawai" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "dokter" ADD FOREIGN KEY ("spesialis") REFERENCES "spesialis" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "dokter" ADD FOREIGN KEY ("document_ijin_prakterk") REFERENCES "file" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "registrasi_pasien" ADD FOREIGN KEY ("pasien_id") REFERENCES "pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "registrasi_pasien" ADD FOREIGN KEY ("dokter_id") REFERENCES "dokter" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "registrasi_pasien" ADD FOREIGN KEY ("poli_id") REFERENCES "poli" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "registrasi_pasien" ADD FOREIGN KEY ("penjamin_id") REFERENCES "penjamin" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "episode_rawat_jalan" ADD FOREIGN KEY ("registrasi_pasien_id") REFERENCES "registrasi_pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "diagnosa_rawat_jalan" ADD FOREIGN KEY ("episode_rawat_jalan_id") REFERENCES "episode_rawat_jalan" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "diagnosa_rawat_jalan" ADD FOREIGN KEY ("diagnosa_id") REFERENCES "diagnosa_icd_10" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "prosedur_rawat_jalan" ADD FOREIGN KEY ("episode_rawat_jalan_id") REFERENCES "episode_rawat_jalan" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "prosedur_rawat_jalan" ADD FOREIGN KEY ("prosedur_id") REFERENCES "prosedur_icd_9" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "antrian_poli" ADD FOREIGN KEY ("registrasi_pasien_id") REFERENCES "registrasi_pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "task_id" ADD FOREIGN KEY ("registrasi_pasien_id") REFERENCES "registrasi_pasien" ("id") DEFERRABLE INITIALLY IMMEDIATE;
