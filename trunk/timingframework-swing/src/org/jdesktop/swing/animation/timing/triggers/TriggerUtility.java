package org.jdesktop.swing.animation.timing.triggers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;

import javax.swing.JComponent;

import org.jdesktop.core.animation.i18n.I18N;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Trigger;
import org.jdesktop.core.animation.timing.triggers.FocusTriggerEvent;
import org.jdesktop.core.animation.timing.triggers.MouseTriggerEvent;
import org.jdesktop.core.animation.timing.triggers.TimingTrigger;
import org.jdesktop.core.animation.timing.triggers.TimingTriggerEvent;

import com.surelogic.Immutable;
import com.surelogic.ThreadSafe;
import com.surelogic.Utility;

/**
 * A utility that creates Swing triggers.
 * 
 * @author Chet Haase
 * @author Tim Halloran
 */
@Immutable
@Utility
public final class TriggerUtility {

  /**
   * Creates a non-auto-reversing timing trigger and adds it as a target to the
   * source animation.
   * 
   * @param source
   *          the animation that will be listened to for events to start the
   *          target animation.
   * @param target
   *          the animation that will start when the event occurs.
   * @param event
   *          the {@link TimingTriggerEvent} on <tt>source</tt> that will cause
   *          <tt>target</tt> to start.
   * @return the resulting trigger.
   * 
   * @throws IllegalArgumentException
   *           if any of the parameters is {@code null}.
   * 
   * @see TimingTrigger
   */
  public static Trigger addTimingTrigger(Animator source, Animator target, TimingTriggerEvent event) {
    return TimingTrigger.addTrigger(source, target, event);
  }

  /**
   * Creates a timing trigger and adds it as a target to the source animation.
   * 
   * @param source
   *          the animation that will be listened to for events to start the
   *          target animation.
   * @param target
   *          the animation that will start when the event occurs.
   * @param event
   *          the {@link TimingTriggerEvent} on <tt>source</tt> that will cause
   *          <tt>target</tt> to start.
   * @param autoReverse
   *          {@code true} if the animation should be reversed on opposite
   *          trigger events, {@code false} otherwise.
   * @return the resulting trigger.
   * 
   * @throws IllegalArgumentException
   *           if any of the parameters is {@code null}.
   * 
   * @see TimingTrigger
   */
  public static Trigger addTimingTrigger(Animator source, Animator target, TimingTriggerEvent event, boolean autoReverse) {
    return TimingTrigger.addTrigger(source, target, event, autoReverse);
  }

  /**
   * Creates an action trigger and adds it as an {@link ActionListener} to the
   * passed object. For example, to have {@code anim} start when a button is
   * clicked, one might write the following:
   * 
   * <pre>
   * Trigger trigger = TriggerUtility.addActionTrigger(button, anim);
   * </pre>
   * 
   * The returned trigger object can be safely ignored if the code never needs
   * to disarm the trigger.
   * 
   * <pre>
   * TriggerUtility.addActionTrigger(button, anim);
   * </pre>
   * 
   * @param object
   *          an object that will be used as an event source for this trigger.
   *          This object must have an {@code addActionListener} method.
   * @param target
   *          the animation that will start when the event occurs.
   * @return the resulting trigger.
   * 
   * @throws IllegalArgumentException
   *           if either of the parameters is {@code null} or if <tt>object</tt>
   *           has no {@code addActionListener} method.
   */
  public static Trigger addActionTrigger(Object object, Animator target) {
    if (object == null)
      throw new IllegalArgumentException(I18N.err(1, "object"));
    if (target == null)
      throw new IllegalArgumentException(I18N.err(1, "target"));
    final ActionTriggerHelper trigger = new ActionTriggerHelper(target);
    try {
      Method addListenerMethod = object.getClass().getMethod("addActionListener", ActionListener.class);
      addListenerMethod.invoke(object, trigger);
    } catch (Exception e) {
      throw new IllegalArgumentException(I18N.err(102, object), e);
    }
    return trigger;
  }

  @ThreadSafe
  private static class ActionTriggerHelper extends Trigger implements ActionListener {

    protected ActionTriggerHelper(Animator animator) {
      super(animator, null, false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      fire(null);
    }
  }

  /**
   * Creates a non-auto-reversing focus trigger and adds it as a
   * {@link FocusListener} to the passed component. For example, to have
   * {@code anim} start when component receives an IN event, one might write the
   * following:
   * 
   * <pre>
   * Trigger trigger = TriggerUtility.addFocusTrigger(component, anim, FocusTriggerEvent.IN);
   * </pre>
   * 
   * The returned trigger object can be safely ignored if the code never needs
   * to disarm the trigger.
   * 
   * <pre>
   * TriggerUtility.addFocusTrigger(component, anim, FocusTriggerEvent.IN);
   * </pre>
   * 
   * @param component
   *          the component that will generate focus events for this trigger.
   * @param target
   *          the animation that will start when the event occurs.
   * @param event
   *          the {@link FocusTriggerEvent} on <tt>component</tt> that will
   *          cause <tt>target</tt> to start.
   * @return the resulting trigger.
   * 
   * @throws IllegalArgumentException
   *           if any of the parameters is {@code null}.
   */
  public static Trigger addFocusTrigger(JComponent component, Animator target, FocusTriggerEvent event) {
    return addFocusTrigger(component, target, event, false);
  }

  /**
   * Creates a focus trigger and adds it as a {@link FocusListener} to the
   * passed component.
   * 
   * @param component
   *          the component that will generate focus events for this trigger.
   * @param target
   *          the animation that will start when the event occurs.
   * @param event
   *          the {@link FocusTriggerEvent} on <tt>component</tt> that will
   *          cause <tt>target</tt> to start.
   * @param autoReverse
   *          {@code true} if the animation should be reversed on opposite
   *          trigger events, {@code false} otherwise.
   * @return the resulting trigger.
   * 
   * @throws IllegalArgumentException
   *           if any of the parameters is {@code null}.
   */
  public static Trigger addFocusTrigger(JComponent component, Animator target, FocusTriggerEvent event, boolean autoReverse) {
    if (component == null)
      throw new IllegalArgumentException(I18N.err(1, "component"));
    if (target == null)
      throw new IllegalArgumentException(I18N.err(1, "target"));
    if (event == null)
      throw new IllegalArgumentException(I18N.err(1, "event"));
    final FocusTriggerHelper trigger = new FocusTriggerHelper(component, target, event, autoReverse);
    trigger.init();
    return trigger;
  }

  @ThreadSafe
  private static class FocusTriggerHelper extends Trigger implements FocusListener {

    private final JComponent f_component;

    protected FocusTriggerHelper(JComponent component, Animator target, FocusTriggerEvent event, boolean autoReverse) {
      super(target, event, autoReverse);
      f_component = component;
    }

    public void init() {
      f_component.addFocusListener(this);
    }

    @Override
    public void disarm() {
      f_component.removeFocusListener(this);
      super.disarm();
    }

    @Override
    public void focusGained(FocusEvent e) {
      fire(FocusTriggerEvent.IN);
    }

    @Override
    public void focusLost(FocusEvent e) {
      fire(FocusTriggerEvent.OUT);
    }
  }

  /**
   * Creates a non-auto-reversing mouse trigger and adds it as a
   * {@link MouseListener} to the passed component. For example, to have
   * {@code anim} start when component receives an CLICK event, one might write
   * the following:
   * 
   * <pre>
   * Trigger trigger = TriggerUtility.addMouseTrigger(component, anim, MouseTriggerEvent.CLICK);
   * </pre>
   * 
   * The returned trigger object can be safely ignored if the code never needs
   * to disarm the trigger.
   * 
   * <pre>
   * TriggerUtility.addMouseTrigger(component, anim, MouseTriggerEvent.CLICK);
   * </pre>
   * 
   * @param component
   *          the component that will generate mouse events for this trigger.
   * @param target
   *          the animation that will start when the event occurs.
   * @param event
   *          the {@link MouseTriggerEvent} on <tt>component</tt> that will
   *          cause <tt>target</tt> to start.
   * @return the resulting trigger.
   * 
   * @throws IllegalArgumentException
   *           if any of the parameters is {@code null}.
   */
  public static Trigger addMouseTrigger(JComponent component, Animator animator, MouseTriggerEvent event) {
    return addMouseTrigger(component, animator, event, false);
  }

  /**
   * Creates a mouse trigger and adds it as a {@link MouseListener} to the
   * passed component.
   * 
   * @param component
   *          the component that will generate mouse events for this trigger.
   * @param target
   *          the animation that will start when the event occurs.
   * @param event
   *          the {@link MouseTriggerEvent} on <tt>component</tt> that will
   *          cause <tt>target</tt> to start.
   * @param autoReverse
   *          {@code true} if the animation should be reversed on opposite
   *          trigger events, {@code false} otherwise.
   * @return the resulting trigger.
   * 
   * @throws IllegalArgumentException
   *           if any of the parameters is {@code null}.
   */
  public static Trigger addMouseTrigger(JComponent component, Animator target, MouseTriggerEvent event, boolean autoReverse) {
    if (component == null)
      throw new IllegalArgumentException(I18N.err(1, "component"));
    if (target == null)
      throw new IllegalArgumentException(I18N.err(1, "target"));
    if (event == null)
      throw new IllegalArgumentException(I18N.err(1, "event"));
    final MouseTriggerHelper trigger = new MouseTriggerHelper(component, target, event, autoReverse);
    trigger.init();
    return trigger;
  }

  private static class MouseTriggerHelper extends Trigger implements MouseListener {

    private final JComponent f_component;

    protected MouseTriggerHelper(JComponent component, Animator target, MouseTriggerEvent event, boolean autoReverse) {
      super(target, event, autoReverse);
      f_component = component;
    }

    public void init() {
      f_component.addMouseListener(this);
    }

    @Override
    public void disarm() {
      f_component.removeMouseListener(this);
      super.disarm();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      fire(MouseTriggerEvent.CLICK);
    }

    @Override
    public void mousePressed(MouseEvent e) {
      fire(MouseTriggerEvent.PRESS);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      fire(MouseTriggerEvent.RELEASE);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      fire(MouseTriggerEvent.ENTER);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      fire(MouseTriggerEvent.EXIT);
    }
  }

  private TriggerUtility() {
    throw new AssertionError();
  }
}
