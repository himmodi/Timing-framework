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

package org.jdesktop.animation.timing.examples;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import org.jdesktop.animation.timing.*;
import org.jdesktop.animation.timing.interpolation.ObjectModifier;
import org.jdesktop.animation.timing.interpolation.PropertyRange;
import org.jdesktop.animation.timing.triggers.ButtonStateEvent;
import org.jdesktop.animation.timing.Envelope.EndBehavior;
import org.jdesktop.animation.timing.Envelope.RepeatBehavior;
import org.jdesktop.animation.timing.triggers.ComponentFocusEvent;
import org.jdesktop.animation.timing.triggers.ActionTrigger;
import org.jdesktop.animation.timing.triggers.ButtonStateTrigger;
import org.jdesktop.animation.timing.triggers.ComponentFocusTrigger;
import org.jdesktop.animation.timing.triggers.Trigger;
import org.jdesktop.animation.timing.triggers.Trigger.TriggerAction;

/**
 * Demonstrates simple animation effects triggered by events.  Events
 * trigger animated painting of some objects in the window (not the
 * buttons themselves, just to simplify the example code).
 * 
 * @author Chet
 */
public class Triggers {
    
    /** Creates a new instance of Triggers */
    public Triggers() {
    }
    
    private static void createAndShowGUI() {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        JFrame f = new JFrame("Timing Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout());
        
        // Create panel with buttons
        f.setSize(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        setupButtons(panel);
        
        f.add(panel, BorderLayout.CENTER);
        f.setVisible(true);
    }
    
    private static void setupButtons(JPanel panel) {
        Color inactiveColor = Color.lightGray;
        Color activeColor = Color.yellow;
        // Create and add them to the panel
        JButton button = new JButton("Trigger Button");
        panel.add(button);
        button.setBounds(0, 0, 150, 30);
        JButton otherButton = new JButton("Other Button");
        panel.add(otherButton);
        otherButton.setBounds(0, 40, 150, 30);
        
        // Create the labels where the effects will animate
        JLabel rollover = new JLabel("Rollover");
        rollover.setBounds(200, 0, 100, 50);
        rollover.setOpaque(true);
        rollover.setBackground(inactiveColor);
        JLabel focus = new JLabel("Focus");
        focus.setBounds(200, 50, 100, 50);
        focus.setOpaque(true);
        focus.setBackground(inactiveColor);
        JLabel action = new JLabel("Action");
        action.setBounds(200, 100, 100, 50);
        action.setOpaque(true);
        action.setBackground(inactiveColor);
        JLabel armed = new JLabel("Armed");
        armed.setBounds(200, 150, 100, 50);
        armed.setOpaque(true);
        armed.setBackground(inactiveColor);
        panel.add(rollover);
        panel.add(focus);
        panel.add(action);
        panel.add(armed);
        
        // Create a hover effect for button1
        PropertyRange range = PropertyRange.createPropertyRangeColor("background", 
                activeColor);
        ObjectModifier modifier = new ObjectModifier(rollover, range);
        TimingController timerStart = new TimingController(1000, modifier);
        range = PropertyRange.createPropertyRangeColor("background", 
                inactiveColor);
        modifier = new ObjectModifier(rollover, range);
        TimingController timerStop = new TimingController(1000, modifier);
        Trigger trigger = new ButtonStateTrigger(timerStart, button, 
                ButtonStateEvent.ROLLOVER,
                timerStop);

        // Create a click effect
        range = PropertyRange.createPropertyRangeColor("background", 
                activeColor);
        modifier = new ObjectModifier(action, range);
        Cycle cycle = new Cycle(1000, 30);
        Envelope envelope = new Envelope(1, 0, RepeatBehavior.FORWARD,
                EndBehavior.RESET);
        TimingController timer = new TimingController(cycle, envelope, modifier);
        trigger = new ActionTrigger(timer, button, TriggerAction.START);

        // Create a focus effect
        range = PropertyRange.createPropertyRangeColor("background", 
                activeColor);
        modifier = new ObjectModifier(focus, range);
        timerStart = new TimingController(1000, modifier);
        range = PropertyRange.createPropertyRangeColor("background", 
                inactiveColor);
        modifier = new ObjectModifier(focus, range);
        timerStop = new TimingController(1000, modifier);
        trigger = new ComponentFocusTrigger(timerStart, button, 
                ComponentFocusEvent.FOCUS_IN, 
                timerStop);

        // Create an armed effect
        range = PropertyRange.createPropertyRangeColor("background", 
                activeColor);
        modifier = new ObjectModifier(armed, range);
        timerStart = new TimingController(1000, modifier);
        range = PropertyRange.createPropertyRangeColor("background", 
                inactiveColor);
        modifier = new ObjectModifier(armed, range);
        timerStop = new TimingController(1000, modifier);
        trigger = new ButtonStateTrigger(timerStart, button, 
                ButtonStateEvent.ARMED, 
                timerStop);
        PropertyChangeListener list = new PropertyChangeTriggerListener();
        button.addPropertyChangeListener(list);
    }

    static class PropertyChangeTriggerListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent event) {
            System.out.println("Triggers.propertyChange: event = " + event);
        }
    }
    
    public static void main(String[] args) {
        // Need to do GUI stuff like making the JFrame visible on the
        // Event Dispatch Thread; do this via invokeLater()
        
        Runnable doCreateAndShowGUI = new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        };
        SwingUtilities.invokeLater(doCreateAndShowGUI);
    }
    
}