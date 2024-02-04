package com.test.ElevatorManagement.Model;

public class AddJobWorker implements Runnable {

	private Elevator elevator;
	private Request request;

	public AddJobWorker(Elevator elevator, Request request) {
		this.elevator = elevator;
		this.request = request;
	}

	@Override
	public void run() {
		elevator.addJob(request);
	}

}
