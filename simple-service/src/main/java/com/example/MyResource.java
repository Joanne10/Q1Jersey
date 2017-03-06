package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("q1")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt(@QueryParam("key") String y, @QueryParam("message") String c) {
        StringBuilder resp = new StringBuilder();
        String x = "12389084059184098308123098579283204880956800909293831223134798257496372124879237412193918239183928140";
        //String y = "1239793247987948712739187492308012309184023849817397189273981723912221";
        String z = y;
        //String c = "QTGXGTHWEQENWQVKPIRFO";

        //Decode Z
        for (int offset = 0; offset + z.length() <= x.length(); offset++) {
            StringBuilder tmp = new StringBuilder();
            for (int i = 0; i < z.length(); i++) {
                int i1 = x.charAt(offset + i) - '0';
                int i2 = z.charAt(i) - '0';
                int res = (i1 + i2) % 10;
                tmp.append(res);
            }
            z = tmp.toString();
        }

        //Get K from Z
        BigInteger zi = new BigInteger(z);
        int k = 1 + zi.mod(new BigInteger("25")).intValue();
        
        //Get I from C
        //int n = 1;
        //while (n * (n + 1) < c.length() * 2) n++;
        int n = (int) Math.sqrt((2 * c.length()));
        if ((n * (n + 1)) / 2 != c.length()) {
            return "ERROR!";
        }
        
        ArrayList<char[]> list = new ArrayList<char[]>();
        for (int i = 1; i <= n; i++) list.add(new char[i]);
        int at = 0;
        int start = 0;
        int end = n - 1;
        while (start < end) {
            for (int i = start; i < end - start; i++) {
                list.get(end)[i] = c.charAt(at);
                at++;
            }
            //System.out.print(at + " ");
            for (int i = end; i > 2 * start; i--) {
                list.get(i)[i - start] = c.charAt(at);
                at++;
            }
            //System.out.print(at + " ");
            for (int i = 2 * start; i < end; i++) {
                list.get(i)[start] = c.charAt(at);
                at++;
            }
            //System.out.print(at + " ");
            start++;
            end--;
        }

        //System.out.println();
        if (at < c.length()) list.get(2 * (n - 1) / 3)[(n - 1) / 3] = c.charAt(at);

        //Decode final message
        for (char[] l : list) {
            for (int i = 0; i < l.length; i++) {
                int offset = l[i] - 'A';
                offset -= k;
                if (offset < 0) offset += 26;
                l[i] = (char) ('A' + offset);
            }
            resp.append(new String(l));
            System.out.println(new String(l));
        }
        return resp.toString();
    }
}
