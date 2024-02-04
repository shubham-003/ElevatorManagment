package com.test.ElevatorManagement.Model;

import java.util.TreeSet;

import com.test.ElevatorManagement.Model.Extras.Direction;
import com.test.ElevatorManagement.Model.Extras.State;

public class Elevator {
	private Direction currentDirection = Direction.UP;
	private State currentState = State.IDLE;
	private int currentFloor = 0;

	/**
	 * jobs which are being processed
	 */
	private TreeSet<Request> currentJobs = new TreeSet<>();
	
	/**
	 * up jobs which cannot be processed now so put in pending queue
	 */
	private TreeSet<Request> upPendingJobs = new TreeSet<>();
	
	/**
	 * down jobs which cannot be processed now so put in pending queue
	 */
	private TreeSet<Request> downPendingJobs = new TreeSet<>();

	public void startElevator() {
		while (true) {

			if (checkIfJob()) {

				if (currentDirection == Direction.UP) {
					// poll the lowest available next floor
					Request request = currentJobs.pollFirst();
					// process this request and bring to the required floor
					processUpRequest(request);
					if (currentJobs.isEmpty()) {
						addPendingDownJobsToCurrentJobs();

					}

				}
				if (currentDirection == Direction.DOWN) {
					Request request = currentJobs.pollLast();
					processDownRequest(request);
					if (currentJobs.isEmpty()) {
						addPendingUpJobsToCurrentJobs();
					}

				}
			}
		}
	}

	public boolean checkIfJob() {

		if (currentJobs.isEmpty()) {
			return false;
		}
		return true;

	}

	private void processUpRequest(Request request) {
		// The elevator is not on the floor where the person has requested it i.e. source floor. So first bring it there.
		int startFloor = currentFloor;
		if (startFloor < request.getExternalRequest().getSourceFloor()) {
			for (int i = startFloor; i <= request.getExternalRequest().getSourceFloor(); i++) {
				System.out.println("We have reached floor -- " + i);
				currentFloor = i;
			}
		}
		// The elevator is now on the floor where the person has requested it i.e. source floor. User can enter and go to the destination floor.
		System.out.println("Reached Source Floor--opening door");

		startFloor = currentFloor;

		for (int i = startFloor; i <= request.getInternalRequest().getDestinationFloor(); i++) {
			System.out.println("We have reached floor -- " + i);
			currentFloor = i;
			if (checkIfNewJobCanBeProcessed(request)) {
				break;
			}
		}

	}

	private void processDownRequest(Request request) {

		int startFloor = currentFloor;
		if (startFloor < request.getExternalRequest().getSourceFloor()) {
			for (int i = startFloor; i <= request.getExternalRequest().getSourceFloor(); i++) {
				System.out.println("We have reached floor -- " + i);
				currentFloor = i;
			}
		}

		System.out.println("Reached Source Floor--opening door");

		startFloor = currentFloor;

		for (int i = startFloor; i >= request.getInternalRequest().getDestinationFloor(); i--) {
			System.out.println("We have reached floor -- " + i);
			currentFloor = i;
			if (checkIfNewJobCanBeProcessed(request)) {
				break;
			}
		}

	}

	private boolean checkIfNewJobCanBeProcessed(Request currentRequest) {
		if (checkIfJob()) {
			if (currentDirection == Direction.UP) {
				Request request = currentJobs.pollFirst();
				if (request.getInternalRequest().getDestinationFloor() < currentRequest.getInternalRequest()
						.getDestinationFloor()) {
					currentJobs.add(request);
					currentJobs.add(currentRequest);
					return true;
				}
				currentJobs.add(request);

			}

			if (currentDirection == Direction.DOWN) {
				Request request = currentJobs.pollLast();
				if (request.getInternalRequest().getDestinationFloor() > currentRequest.getInternalRequest()
						.getDestinationFloor()) {
					currentJobs.add(request);
					currentJobs.add(currentRequest);
					return true;
				}
				currentJobs.add(request);

			}

		}
		return false;

	}

	private void addPendingDownJobsToCurrentJobs() {
		if (!downPendingJobs.isEmpty()) {
			currentJobs = downPendingJobs;
			currentDirection = Direction.DOWN;
		} else {
			currentState = State.IDLE;
		}

	}

	private void addPendingUpJobsToCurrentJobs() {
		if (!upPendingJobs.isEmpty()) {
			currentJobs = upPendingJobs;
			currentDirection = Direction.UP;
		} else {
			currentState = State.IDLE;
		}

	}
	
	
	public void addJob(Request request) {
		if (currentState == State.IDLE) {
			currentState = State.MOVING;
			currentDirection = request.getExternalRequest().getDirectionToGo();
			currentJobs.add(request);
		} else if (currentState == State.MOVING) {

			if (request.getExternalRequest().getDirectionToGo() != currentDirection) {
				addtoPendingJobs(request);
			} else if (request.getExternalRequest().getDirectionToGo() == currentDirection) {
				if (currentDirection == Direction.UP
						&& request.getInternalRequest().getDestinationFloor() < currentFloor) {
					addtoPendingJobs(request);
				} else if (currentDirection == Direction.DOWN
						&& request.getInternalRequest().getDestinationFloor() > currentFloor) {
					addtoPendingJobs(request);
				} else {
					currentJobs.add(request);
				}

			}

		}

	}

	public void addtoPendingJobs(Request request) {
		if (request.getExternalRequest().getDirectionToGo() == Direction.UP) {
			System.out.println("Add to pending up jobs");
			upPendingJobs.add(request);
		} else {
			System.out.println("Add to pending down jobs");
			downPendingJobs.add(request);
		}
	}
}
