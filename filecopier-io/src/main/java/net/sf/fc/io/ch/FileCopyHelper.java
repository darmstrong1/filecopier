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

import java.io.File;

import net.sf.fc.io.FileCopyException;
import net.sf.fc.io.event.FileCopyEventListener;
import net.sf.fc.io.event.FileCopyEventSupport;

import static net.sf.fc.io.event.FileCopyEvent.CopyStatus;
import static net.sf.fc.io.event.FileCopyEvent.Level;

/**
 *
 * Abstract class that provides callback methods to {@link org.apache.commons.io.FileCopier}.
 * Extensions to FileCopyHelper provide methods to handle existing files
 * during copy operations, exceptions, status reporting, and a cancel method.
 * <p>
 *
 * @author David Armstrong
 */
public abstract class FileCopyHelper {

    protected final FileCopyEventSupport eventSupport;

    /**
     * Enum that is returned from handleExistingFile method
     * @author David Armstrong
     *
     */
    public static enum Action {

        /**
         * <tt>CONTINUE</tt> means the FileCopyHelper took some action to allow the copy to take place.
         */
        CONTINUE,

        /**
         * <tt>SKIP</tt> means the copy should be skipped.
         */
        SKIP,

        /**
         * <tt>ABEND</tt> means the FileCopyHelper tried to take some action to allow the copy to take place, but it failed.
         */
        ABEND
    }

    /**
     * Default constructor.
     */
    public FileCopyHelper() {
        eventSupport = new FileCopyEventSupport(this);
    }

    /**
     * Method that handles destination files that exist during a copy operation.
     * This method returns true if <tt>FileCopier</tt> should move forward with
     * the copy operation. If this method does nothing but return true,
     * <tt>FileCopier</tt> will overwrite the file. Implementations of this method
     * like {@link org.apache.commons.io.filecopyhelper.RenameAppendFileCopyHelper}
     * rename the existing file and return true.
     *
     * @param dest the destination file, which exists.
     * @param source the source file.
     * @return true to signal to <tt>FileCopier</tt> to move forward with the
     * copy, false to tell it to stop.
     */
    public abstract Action handleExistingFile(final File source, final File dest);

    /**
     * Method that handles {@link org.apache.commons.io.FileCopyException} thrown
     * during an attempt to copy a File to another File. This method allows for
     * a last-ditch attempt to copy the file as the FileCopyException object
     * contains the source and destination file. For instance, in windows systems
     * if an attempt is made to overwrite a file that is in use, the system will
     * throw an error. For an implementation of <tt>FileCopyHelper</tt> whose
     * handleExistingFile method simply returns true, allowing for an overwrite
     * attempt, the handleFileCopyException method could rename the destination
     * file and make the copy attempt again.
     * @param e the FileCopyException to handle
     * @return true if the copy is attempted again and it is successful,
     * otherwise, false.
     */
    public boolean handleFileCopyException(final FileCopyException e) {
        return false;
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
        eventSupport.fireStatusChanged(srcFile, dstFile, status, level, bytesCopied);
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
        eventSupport.fireStatusChanged(srcFile, dstFile, status, level, bytesCopied, throwable);
    }

    /**
     * Method that adds a FileCopyEventListner object. FileCopyEventListners receive events
     * that report the status and progress of copy operations.
     * @param listener
     */
    public void addFileCopyEventListener(FileCopyEventListener listener) {
        eventSupport.addFileCopyEventListener(listener);
    }


    /**
     * Method that removes a FileCopyEventListner object. FileCopyEventListners receive events
     * that report the status and progress of copy operations.
     * @param listener
     */
    public void removeFileCopyEventListener(FileCopyEventListener listener) {
        eventSupport.removeFileCopyEventListener(listener);
    }

    /**
     * This method returns true if subsequent copy operations should be abandoned.
     * Ths method is called by <tt>FileCopier</tt> at the end of each copy operation.
     * @return true if subsequent copy operations should be cancelled.
     */
    public abstract boolean cancel();
}
