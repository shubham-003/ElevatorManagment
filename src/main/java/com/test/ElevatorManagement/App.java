package com.test.ElevatorManagement;

import com.test.ElevatorManagement.Model.AddJobWorker;
import com.test.ElevatorManagement.Model.Elevator;
import com.test.ElevatorManagement.Model.ExternalRequest;
import com.test.ElevatorManagement.Model.InternalRequest;
import com.test.ElevatorManagement.Model.ProcessJobWorker;
import com.test.ElevatorManagement.Model.Request;
import com.test.ElevatorManagement.Model.Extras.Direction;

public class App 
{
    public static void main( String[] args )
    {
    	Elevator elevator = new Elevator();

		/**
		 * Thread for starting the elevator
		 */
		ProcessJobWorker processJobWorker = new ProcessJobWorker(elevator);
		Thread t2 = new Thread(processJobWorker);
		t2.start();
//		processJobWorker.run();

		ExternalRequest er = new ExternalRequest(Direction.DOWN, 5);

		InternalRequest ir = new InternalRequest(0);

		Request request1 = new Request(ir, er);

		/**
		 * Pass job to the elevator
		 */
		
		new Thread(new AddJobWorker(elevator, request1)).start();
		new Thread(new AddJobWorker(elevator, new Request(new InternalRequest(7), new ExternalRequest(Direction.UP, 3)))).start();
		new Thread(new AddJobWorker(elevator, new Request(new InternalRequest(4), new ExternalRequest(Direction.UP, 2)))).start();
    }
}
