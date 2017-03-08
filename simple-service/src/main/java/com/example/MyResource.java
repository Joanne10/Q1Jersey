package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import java.math.BigInteger;
import java.util.*;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("q1")
public class MyResource {
    
    private final String x = "12389084059184098308123098579283204880956800909293831223134798257496372124879237412193918239183928140";

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt(@QueryParam("key") String y, @QueryParam("message") String c) {
        // void case
        if (c == null || c.length() == 0) {
            return "NULL";
        }
        
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
        // get the "edge"
        int n = (int) Math.sqrt((2 * c.length()));
        
        // check equilateral triangle
        if ((n * (n + 1)) / 2 != c.length()) {
            return "ERROR!";
        }
        
        String res1 = decodeHelper1(k, n, c);
        System.out.println(res1);
        // String res2 = decodeHelper2(k, n, c);
        try {
            String res2 = decodeHelper2(k, n, c);
            System.out.println(res2);
            if (res1.equals(res2)) {
                return res1;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "Algorithm needs further check!";
    }
    
    private String decodeHelper1(int k, int n, String c) {
        StringBuilder resp = new StringBuilder();
        
        // intialize the triangle
        List<char[]> list = new ArrayList<char[]>();
        for (int i = 1; i <= n; i++) {
            list.add(new char[i]);
        }
        
        int at = 0;
        int start = 0;
        int end = n - 1;
        while (start < end) {
            for (int i = start; i < end - start; i++) {
                list.get(end)[i] = c.charAt(at++);
            }
            //System.out.print(at + " ");
            for (int i = end; i > 2 * start; i--) {
                list.get(i)[i - start] = c.charAt(at++);
            }
            //System.out.print(at + " ");
            for (int i = 2 * start; i < end; i++) {
                list.get(i)[start] = c.charAt(at++);
            }
            //System.out.print(at + " ");
            start++;
            end--;
        }

        // edge case
        //System.out.println();
        if (at < c.length()) {
            list.get(2 * (n - 1) / 3)[(n - 1) / 3] = c.charAt(at);
        }

        //Decode final message
        for (char[] l : list) {
            for (int i = 0; i < l.length; i++) {
                int offset = l[i] - 'A';
                offset -= k;
                if (offset < 0) {
                    offset += 26;
                }
                l[i] = (char) ('A' + offset);
            }
            resp.append(new String(l));
            System.out.println(new String(l));
        }
        return resp.toString();
    }
    
    private String decodeHelper2(int k, int height, String message) {
        // set the triangle
        // (0, 0)
        // *
        // * *
        // * * *
        // * * * *
        // * * * * *
        // * * * * * *
        List<char[]> triangle = new ArrayList<char[]>();
        for (int i = 1; i <= height; i++) {
            triangle.add(new char[i]);
        }
        
        // set the directions
        int[][] directions = new int[3][2];
        directions[0] = new int[]{1, 0}; /* down */
        directions[1] = new int[]{0, 1}; /* right */
        directions[2] = new int[]{-1, -1}; /* left-up */
        
        // fill the triangle
        // (0, 0)
        // P
        // Q K
        // R L G
        // S M H D
        // T N I E B
        // U O J F C A
        int direction = 0, pos = 0;
        int row = -1, col = 0;
        for (int len = height; len >= 1; len--) {
            int[] direct = directions[direction];
            for (int i = 0; i < len; i++) {
                row += direct[0];
                col += direct[1];
                triangle.get(row)[col] = message.charAt(pos++);
            }
            direction++;
            if (direction == directions.length) {
                direction = 0;
            }
        }
        
        /*
        // check the structure of the triangle
        for (int i = 0; i < triangle.size(); i++) {
            for (int j = 0; j < triangle.get(i).length; j++) {
                System.out.print(triangle.get(i)[j] + " - ");
            }
            System.out.println();
        }
        */

        StringBuilder res = new StringBuilder();
        for (int j = height - 1; j >= 0; j--) {
            for (int i = j; i < height; i++) {
                int offset = triangle.get(i)[j] - 'A';
                offset -= k;
                if (offset < 0) {
                    offset += 26;
                }
                char c = (char) ('A' + offset);
                //System.out.print(c + " - ");
                res.append((char) (c));
            }
        }
        return res.toString();
    }
}
