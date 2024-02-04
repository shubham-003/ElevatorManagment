package com.test.ElevatorManagement.Service;

import com.test.ElevatorManagement.Model.Elevator;

public class ProcessJobWorker implements Runnable {

	private Elevator elevator;

	public ProcessJobWorker(Elevator elevator) {
		this.elevator = elevator;
	}

	@Override
	public void run() {
		/**
		 * start the elevator
		 */
		elevator.startElevator();
	}

}
