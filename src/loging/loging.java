
package loging;
import java.util.logging.*;
import java.io.*;

public class loging {
    public class ActivityLogger {

        private static final Logger logger = Logger.getLogger(ActivityLogger.class.getName());

        static {
            try {
                FileHandler fileHandler = new FileHandler("activity.log", true);
                fileHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(fileHandler);
            } catch (IOException e) {
                System.err.println("Error setting up file handler for logging: " + e.getMessage());
            }
        }

        // Log untuk aktivitas login
        public static void logLogin(String username) {
            logger.info(username + " berhasil melakukan login.");
        }

        public static void logLogout(String username){
            logger.info(username + " telah melakukan loggout.");
        }

        // Log untuk aktivitas insert user
        public static void logInsertUser(String username, String nama) {
            logger.info(username + " telah menambahkan user: " + nama);
        }

        // Log untuk aktivitas edit user
        public static void logEditUser(String username, String nama) {
            logger.info(username + " telah mengedit data: " + nama);
        }

        // Log untuk aktivitas delete user
        public static void logDeleteUser(String username, String nama) {
            logger.info(username + " telah menghapus: " + nama);
        }

    //    log utntuk pencarian user
        public static void logSearching(String username, String nama){
            logger.info(username + " telah mencari nama " + nama);
        }


    //  log untuk input user 
        public static void logInsertProduk(String username, String namaProduk){
            logger.info(username + " telah menambahkan produk : " + namaProduk);
        }

        // log untuk aktivitas edit produk
        public static void logEditProduk(String username, String namaProduk){
            logger.info(username + " telah mengedit data di dalam produk: " + namaProduk);
        }

        //log aktivitas hapus produk
        public static void logDeleteProduk(String username, String namaProduk){
            logger.info(username + " telah menghapus produk: " + namaProduk);
        } 

        // log aktivitas cari produk
        public static void logSearchProduk(String username, String idProduk){
            logger.info(username + " mencari produk dengan id produk: " + idProduk);
        }

        // Log untuk mencatat error
        public static void logError(String errorMessage) {
            logger.severe("Error: " + errorMessage);
        }
    }
}
