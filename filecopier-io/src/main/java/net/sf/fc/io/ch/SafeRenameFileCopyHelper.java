/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package net.sf.fc.io.ch;

import net.sf.fc.io.FileCopyException;
import net.sf.fc.io.event.FileCopyEventListener;
import net.sf.fc.io.event.FileCopyEvent.CopyStatus;
import net.sf.fc.io.event.FileCopyEvent.Level;

import java.io.File;

/**
 *
 * Class that serves as a decorator to a class that implements RenameFileCopyHelper.
 * <p>
 * This class's <tt>handleExistingFile</tt> method calls the <tt>createNewFile</tt>
 * method of the RenameFileCopyHelper object it decorates and ensures that the
 * File object created does not exist on disk before renaming the existing
 * destination file.
 *
 * @author David Armstrong
 */
public class SafeRenameFileCopyHelper extends RenameFileCopyHelper {

    private final RenameFileCopyHelper renameFileCopyHelper;
    private final int retryLimit;
    private final char separator;

    private static final int defaultRetryLimit = Integer.MAX_VALUE-1;

    /**
     * Constructor that takes the {@link RenameFileCopyHelper} implementation
     * that this object will decorate.
     *
     * @param renameFileCopyHelper RenameFileCopyHelper instance
     */
    public SafeRenameFileCopyHelper(RenameFileCopyHelper renameFileCopyHelper) {
        if(renameFileCopyHelper == null) {
            throw new NullPointerException("RenameFileCopyHelper must not be null");
        }
        this.renameFileCopyHelper = renameFileCopyHelper;
        this.retryLimit = defaultRetryLimit;
        this.separator = '.';
    }

    /**
     * Constructor that takes the <tt>RenameFileCopyHelper</tt> implementation
     * that this object will decorate. Also, takes a retryLimit value for retrying
     * the rename of the existing file if the renamed file also exists. The
     * default retry limit is Integer.MAX_VALUE-1
     * @param renameFileCopyHelper RenameFileCopyHelper instance
     * @param retryLimit the max number of times to attempt a rename.
     */
    public SafeRenameFileCopyHelper(RenameFileCopyHelper renameFileCopyHelper, int retryLimit) {
        if(renameFileCopyHelper == null) {
            throw new NullPointerException("RenameFileCopyHelper must not be null");
        }
        if(retryLimit < 1) {
            throw new IllegalArgumentException("Retry limit must be at least 1");
        }
        this.renameFileCopyHelper = renameFileCopyHelper;
        this.retryLimit = retryLimit == Integer.MAX_VALUE ? defaultRetryLimit : retryLimit;
        this.separator = '.';
    }

    /**
     * Constructor that takes the <tt>RenameFileCopyHelper</tt> implementation
     * that this object will decorate. Also, takes char separator. This separator
     * will be inserted between the end of the new name for the file and a
     * number that increments each time a file with the new name exists. The
     * default char separator is '.'.
     * @param renameFileCopyHelper RenameFileCopyHelper instance
     * @param separator separator before incremented number at end of new name
     */
    public SafeRenameFileCopyHelper(RenameFileCopyHelper renameFileCopyHelper, char separator) {
        if(renameFileCopyHelper == null) {
            throw new NullPointerException("RenameFileCopyHelper must not be null");
        }
        this.renameFileCopyHelper = renameFileCopyHelper;
        this.retryLimit = defaultRetryLimit;
        this.separator = separator;
    }

    /**
     * Constructor that takes the <tt>RenameFileCopyHelper</tt> implementation
     * that this object will decorate. Also, takes a retryLimit value for retrying
     * the rename of the existing file if the renamed file also exists. The
     * default retry limit is Integer.MAX_VALUE-1Also, takes char separator. This
     * separator will be inserted between the end of the new name for the file and
     * a number that increments each time a file with the new name exists. The
     * default char separator is '.'.
     * @param renameFileCopyHelper RenameFileCopyHelper instance
     * @param retryLimit the max number of times to attempt a rename.
     * @param separator separator before incremented number at end of new name
     */
    public SafeRenameFileCopyHelper(RenameFileCopyHelper renameFileCopyHelper, int retryLimit, char separator) {
        if(renameFileCopyHelper == null) {
            throw new NullPointerException("RenameFileCopyHelper must not be null");
        }
        if(retryLimit < 1) {
            throw new IllegalArgumentException("Retry limit must be at least 1");
        }
        this.renameFileCopyHelper = renameFileCopyHelper;
        this.retryLimit = retryLimit == Integer.MAX_VALUE ? defaultRetryLimit : retryLimit;
        this.separator = separator;
    }

    /**
     * This method calls the <tt>RenameFileCopyHelper</tt> object's <tt>createNewName</tt>
     * method. It then checks to see if the File that the <tt>createNewName</tt>
     * method returned exists. If the file exists, it increments a count and puts
     * that number at the end of the name of the file after the char separator,
     * which is by default '.'. It will continue this until it finds a name of
     * a file that does not exist or it hits the retry limit, which is by default
     * Integer.MAX_VALUE-1.
     * @param source the source file
     * @param dest the destination file, which exists
     * @return Action.CONTINUE if the destination file is successfully renamed.
     */
    public Action handleExistingFile(final File source, final File dest) {

        Action action = Action.CONTINUE;
        boolean status = false;
        File newFile = createNewFile(dest);
        String baseName = newFile.getName();

        for(int i = 1; i <= retryLimit; i++) {
            if(newFile.exists()) {
                newFile = new File(newFile.getParent(), baseName + separator + i);
            } else {
                status = dest.renameTo(newFile);
                break;
            }
        }
        if(status) {
            renameFileCopyHelper.reportCopyEvent(dest, newFile, CopyStatus.RENAME_COMPLETE, Level.INFO, newFile.length());
        } else {
            renameFileCopyHelper.reportCopyEvent(dest, newFile, CopyStatus.RENAME_FAILED, Level.ERROR, 0);
            action = Action.ABEND;
        }
        return action;
    }

    /**
     * Method that handles {@link org.apache.commons.io.FileCopyException} thrown
     * during an attempt to copy a File to another File. This implementation
     * passes the FileCopyException method to StatusReporter's error method for
     * error handling and returns false to signify that no further copy attempts
     * were made.
     * @param e the FileCopyException to handle
     * @return false.
     */
    public boolean handleFileCopyException(final FileCopyException e) {
        return renameFileCopyHelper.handleFileCopyException(e);
    }

    /**
     * Method that sends data of a CopyEvent to the CopyEventListeners
     * @param srcFile the source file
     * @param dstFile the destination file
     * @param status the status of the event
     * @param level the severity of the event
     * @param bytesCopied the number of bytes copied if this is a progress event
     */
    public void reportCopyEvent(File srcFile, File dstFile, CopyStatus status, Level level, long bytesCopied) {
        renameFileCopyHelper.reportCopyEvent(srcFile, dstFile, status, level, bytesCopied);
    }

    /**
     * Method that sends data of a CopyEvent to the CopyEventListeners
     * @param srcFile the source file
     * @param dstFile the destination file
     * @param status the status of the event
     * @param level the severity of the event
     * @param bytesCopied the number of bytes copied if this is a progress event
     * @param throwable an exception that was thrown during a copy operation
     */
    public void reportCopyEvent(File srcFile, File dstFile, CopyStatus status, Level level, long bytesCopied, Throwable throwable) {
        renameFileCopyHelper.reportCopyEvent(srcFile, dstFile, status, level, bytesCopied, throwable);
    }

    /**
     * Method that adds a FileCopyEventListner object. FileCopyEventListners receive events
     * that report the status and progress of copy operations.
     * @param listener
     */
    public void addFileCopyEventListener(FileCopyEventListener listener) {
        renameFileCopyHelper.addFileCopyEventListener(listener);
    }


    /**
     * Method that removes a FileCopyEventListner object. FileCopyEventListners receive events
     * that report the status and progress of copy operations.
     * @param listener
     */
    public void removeFileCopyEventListener(FileCopyEventListener listener) {
        renameFileCopyHelper.addFileCopyEventListener(listener);
    }

    /**
     * This method creates a new file with whatever rules the extension to
     * <tt>RenameFileCopyHelper</tt> defines.
     * @param dest
     * @return a File object with the new name. This method does not create the
     * file itself, only the File object. The <tt>handleExistingFile</tt> method
     * will create the file on disk.
     */
    public File createNewFile(final File dest) {
        return renameFileCopyHelper.createNewFile(dest);
    }

    /**
     * This method is called by FileCopy after each copy attempt. If the return
     * value is true, all subsequent copies are cancelled. Note that this
     * method only has an impact if multiple files are being copied in a copy
     * method call. If one file is copied, an attempt to copy it will always be
     * made because FileCopy calls this method at the end of each copy attempt.
     * @return true/false to tell FileCopy whether or not to cancel subsequent
     * copy operations. This implementation always returns false.
     */
    public boolean cancel() { return renameFileCopyHelper.cancel(); }

}
