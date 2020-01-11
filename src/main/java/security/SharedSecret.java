
package security;

import java.security.SecureRandom;

/* This generates a secure random per execution of the server
 * A server restart, will generate a new key, making all existing tokens invalid
 * For production (and if a load-balancer is used) come up with a persistent key strategy */
public class SharedSecret {
    private static byte[] secret;
    public static byte[] getSharedKey() {
      
        //REMOVE BEFORE PRODUCTION
        //if(true){
        //    return "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes();
        //}
        if (secret == null) {  //Or better read as an environment variable set on production server
            secret = new byte[32];
            new SecureRandom().nextBytes(secret);
        }
        return secret;
    }
}