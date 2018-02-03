package holder;

import java.util.HashMap;

public class NumbersHolder {
    private static HashMap<String, String> storage = new HashMap<String, String>(1000);

    public static void putNumber(String number, String ani){
        storage.put(number, ani);
    }

   public static boolean contains(String number, String ani){
        if (storage.containsKey(number)){
            if (storage.get(number).equals(ani)){
                return true;
            }
        }
        return false;
   }

   public static void clear(){
       storage.clear();
   }
}
