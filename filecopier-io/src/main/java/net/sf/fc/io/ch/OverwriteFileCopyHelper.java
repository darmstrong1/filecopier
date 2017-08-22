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

/**
 *
 * Class that extends {@link FileCopyHelper}. This class can be passed
 * to {@link org.apache.commons.io.FileCopier} to overwrite files that exist.
 * <p>
 * <tt>OverwriteFileCopyHelper's</tt> implementation of <tt>handleExistingFile</tt>,
 * simply returns true, which signals to <tt>FileCopier</tt> that it is ok to
 * proceed with the copy operation.
 *
 * @author David Armstrong
 */
public class OverwriteFileCopyHelper extends FileCopyHelper {

    /**
     * Default constructor.
     * to report the status of copy operations.
     */
    public OverwriteFileCopyHelper() {
        super();
    }

    /**
     * Method that handles destination files that exist during a copy operation.
     * This implementation returns true to singal to <tt>FileCopier</tt> that it
     * should move forward with the copy operation.
     *
     * @param dest the destination file, which exists.
     * @param source the source file.
     * @return Action.CONTINUE to signal to <tt>FileCopier</tt> to move forward with the
     * copy
     */
    @Override
    public Action handleExistingFile(final File source, final File dest) {
        return Action.CONTINUE;
    }

    /**
     * This method returns true if subsequent copy operations should be abandoned.
     * Ths method is called by <tt>FileCopier</tt> at the end of each copy operation.
     * This implementation always returns false.
     * @return true if subsequent copy operations should be cancelled.
     */
    @Override
    public boolean cancel() { return false; }
}
