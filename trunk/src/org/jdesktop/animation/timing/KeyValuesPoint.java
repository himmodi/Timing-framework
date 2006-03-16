/**
 * Copyright (c) 2006, Sun Microsystems, Inc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following 
 *     disclaimer in the documentation and/or other materials provided 
 *     with the distribution.
 *   * Neither the name of the TimingFramework project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package org.jdesktop.animation.timing;

import java.awt.Point;
import java.lang.reflect.Method;

/**
 *
 * @author Chet
 */
class KeyValuesPoint extends KeyValues<Point> {
    
    /** Creates a new instance of KeyValuesInt */
    public KeyValuesPoint(Point... values) {
        for (Point value : values) {
            this.values.add(value);
        }
    }
    
    /**
     * Returns type of values
     */
    public Class<?> getType() {
        return Point.class;
    }

    /**
     * Linear interpolation variant; set the value of the property
     * to be a linear interpolation of the given fraction between
     * the values at i0 and i1
     */
    public void setValue(Object object, Method method, int i0,
            int i1, float fraction) {
        Point value = (Point)values.get(i0).clone();
        if (i0 != i1) {
            Point v0 = values.get(i0);
            Point v1 = values.get(i1);
            value.x += (int)((v1.x - v0.x) * 
                    fraction + .5);
            value.y += (int)((v1.y - v0.y) * 
                    fraction + .5);
        }
        try {
            method.invoke(object, value);
        } catch (Exception e) {
            System.out.println("Problem invoking method in KVFloat.setValue:" + e);
        }
    }   
    
    /**
     * Discrete variant; set the value of the property to be the
     * value at index.
     */
    public void setValue(Object object, Method method, int index) {
        try {
            method.invoke(object, values.get(index));
        } catch (Exception e) {
            System.out.println("Problem invoking method in KVFloat.setValue:" + e);
        }
    }
    
    
}
