/*
 * Copyright (C) 2004-2015 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2015 Polarion Software
 * All Rights Reserved.  No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from Polarion Software.  This notice must be
 * included on all copies, modifications and derivatives of this
 * work.
 *
 * POLARION SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES 
 * ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. POLARION SOFTWARE
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.polarion.diagtool
import java.nio.channels.FileLock;

class FileLockTest {
    private static final String CHECK = "check";
    private static final String LOCK = "lock";
    private static final String STRESS = "stress";
    private static final Collection<String> COMMANDS = Arrays.asList(CHECK, LOCK, STRESS);

    public static final long DEFAULT_DELAY = 0;
    public static final int DEFAULT_TRIES = 0;
    
    public void run(String[] args) {
        List<String> lockInfo = new ArrayList<>();
        for (def i = 0; i < args.length; i++) {
            if (args[i] == "-locktest") {
                i++
                lockInfo.add(args[i++])
                lockInfo.add(args[i++])
                if (args.length > i)
                    lockInfo.add(args[i++])
                if (args.length > i)
                    lockInfo.add(args[i])
            }
        }
        
        if (lockInfo.size < 2) {
            printUsage();
            return;
        }
        String command = lockInfo.get(0);
        String path = lockInfo.get(1);
        long delay = DEFAULT_DELAY;
        if (lockInfo.size > 2) {
            delay = Long.parseLong(lockInfo.get(2));
        }
        int tries = DEFAULT_TRIES;
        if (lockInfo.size > 3) {
            tries = Integer.parseInt(lockInfo.get(3));
        }

        if (!COMMANDS.contains(command)) {
            System.out.println("Unknown command: " + command);
            printUsage();
            return;
        }

        File file = new File(path);

        if (LOCK.equals(command)) {
            lockCommand(file);
        }
        if (CHECK.equals(command)) {
            commandCheck(file);
        }
        if (STRESS.equals(command)) {
            commandStress(file, delay, tries);
        }
    }


    public static void commandStress(File file, long delay, int tries) throws IOException, InterruptedException {
        System.out.println("Stress-testing locking of " + file.getCanonicalPath());
        boolean forever = tries == 0;
        long num = 0;
        long total = 0;
        while (true) {
            long time = stressIteration(file, delay);
            if (!forever) {
                num++;
                total += time;
                tries--;
                if (tries <= 0) {
                    break;
                }
            }
        }
        System.out.println();
        System.out.println(num + " measurements: average=" + (total / (float) num) + " ms");
    }

    private static long stressIteration(File file, long delay) throws FileNotFoundException, IOException, InterruptedException {
        FileOutputStream stream = new FileOutputStream(file);
        System.out.print("l");
        System.out.flush();
        long time = System.currentTimeMillis();
        stream.getChannel().lock();
        time = System.currentTimeMillis() - time;
        System.out.print("L");
        System.out.flush();
        if (delay != 0) {
            System.out.print(".");
            System.out.flush();
            Thread.sleep(delay);
        }
        System.out.print("u");
        System.out.flush();
        stream.close();
        System.out.print("U");
        System.out.flush();
        if (delay != 0) {
            System.out.print(".");
            System.out.flush();
            Thread.sleep(delay);
        }
        return time;
    }

    private static void commandCheck(File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        try {
            FileLock lock = stream.getChannel().tryLock();
            if (lock == null) {
                System.out.println("Locked.");
            } else {
                System.out.println("Not locked.");
                lock.release();
            }
        } finally {
            stream.close();
        }
    }

    private static void lockCommand(File file) throws IOException, InterruptedException {
        FileOutputStream stream = new FileOutputStream(file);
        try {
            System.out.println("Trying to lock " + file.getCanonicalPath() + "...");
            stream.getChannel().lock();
            System.out.println("Locked.");
            while (true) {
                Thread.sleep(10000);
            }
        } finally {
            stream.close();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: run.bat/sh -locktest COMMAND FILE DELAY TRIES");
        System.out.println("  DELAY is " + DEFAULT_DELAY + " when not set explicitly");
        System.out.println("  TRIES is " + DEFAULT_TRIES + " when not set explicitly");
        System.out.println("Supported commands:");
        System.out.println("  lock   - locks the file");
        System.out.println("  check  - checks if the file is locked");
        System.out.println("  stress - locks the file repeatedly");
        System.out.println("           waits for DELAY milliseconds between attempts or not at all if DELAY is 0");
        System.out.println("           will repeat TRIES number of times or forever if TRIES is 0");
    }
    
    public static void main(String[] args) {
        if (args.length < 7) {
            printUsage()
            System.exit(1)
        }
        new FileLockTest().run(args)
    }
}
