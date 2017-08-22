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

import net.sf.fc.io.event.FileCopyEvent.CopyStatus;
import net.sf.fc.io.event.FileCopyEvent.Level;

/**
 * This is the default FileCopyHelper used by FileCopy if no FileCopyHelper
 * implementation is passed to FileCopy.Builder.
 * <p>
 * This implementation of FileCopyHelper is called Safe because it returns false
 * if the file to be copied already exists in the destination.
 *
 * @author David Armstrong
 */

public class SafeFileCopyHelper extends FileCopyHelper {

    /**
     * Default constructor.
     */
    public SafeFileCopyHelper() {
        super();
    }

    /**
     * Method that handles destination files that exist during a copy operation.
     * This implementation returns false to singal to <tt>FileCopier</tt> that it
     * should not attempt the copy operation.
     *
     * @param dest the destination file, which exists.
     * @param source the source file.
     * @return Action.SKIP to signal to <tt>FileCopier</tt> to not move forward with
     * the copy.
     */
    @Override
    public Action handleExistingFile(final File source, final File dest) {
        reportCopyEvent(source, dest, CopyStatus.COPY_SKIP_DST_EXISTS, Level.INFO, 0);
        return Action.SKIP;
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
    @Override
    public boolean cancel() { return false; }
}
