package org.jdesktop.swt.animation.timing.demos.ch14;

import java.util.concurrent.TimeUnit;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.AnimatorBuilder;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.swt.animation.timing.sources.SWTTimingSource;

/**
 * Simple subclass of {@link BasicRace} that uses a
 * {@link AccelerationInterpolator} to give a non-linear motion effect to the
 * car's movement.
 * <p>
 * This demo is discussed in Chapter 14 on pages 363&ndash;364 of <i>Filthy Rich
 * Clients</i> (Haase and Guy, Addison-Wesley, 2008).
 * 
 * @author Chet Haase
 * @author Tim Halloran
 */
public class NonLinearRace extends BasicRace {

  public static void main(String args[]) {
    final Display display = Display.getDefault();
    final Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());

    final TimingSource ts = new SWTTimingSource(15, TimeUnit.MILLISECONDS, display);
    AnimatorBuilder.setDefaultTimingSource(ts);
    ts.init();

    new NonLinearRace(shell, "SWT Non-Linear Race");

    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    ts.dispose();
    display.dispose();
  }

  public NonLinearRace(Shell shell, String appName) {
    super(shell, appName);
  }

  @Override
  protected Animator getAnimator() {
    return new AnimatorBuilder().setDuration(RACE_TIME, TimeUnit.MILLISECONDS)
        .setInterpolator(new AccelerationInterpolator(0.5, 0.2)).addTarget(this).build();
  }
}