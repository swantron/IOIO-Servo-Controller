
package swantron.project.servo;


import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import android.os.Bundle;
import android.widget.SeekBar;



/**
* import ioio.lib.bluetooth.*;
*/
/**
 * http://swantron.com | Joseph Swanson | 2011
 * IOIO Servo main activity
 * Driving servo via Android
 * utilizing PWM output from IOIO
 */

public class MainActivity extends AbstractIOIOActivity {

	private SeekBar seekBar_;
	/**
	 *  Called upon creation for initialization
	 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        seekBar_ = (SeekBar)findViewById(R.id.SeekBar);

        enableUi(false);
    }
    /**
     * Primary thread...runs until interrupted 
     */
	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		/**
		 * define pin
		 */
		private PwmOutput pwmOutput_;
        /**
         * Called every time a connection with IOIO has been established.
         * (opens pins)
         * 
         * @throws ConnectionLostException
         * (if IOIO connection is lost)
         */

		public void setup() throws ConnectionLostException {
			try {
			
				pwmOutput_ = ioio_.openPwmOutput(10, 100);

				enableUi(true);
			} catch (ConnectionLostException e) {
				enableUi(false);
				throw e;
				
			}
		}
        /**
         * Loop section
         */

		public void loop() throws ConnectionLostException {
			try {

				pwmOutput_.setPulseWidth(500 + seekBar_.getProgress() * 2);

				sleep(10);
			} catch (InterruptedException e) {
				ioio_.disconnect();
				
			} catch (ConnectionLostException e) {
				enableUi(false);
				throw e;
				
			}
		}
	}
    /**
     * A method to create our IOIO thread.
     */

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}

	private void enableUi(final boolean enable) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				seekBar_.setEnabled(enable);
		
			}
		});
	}
	

}
