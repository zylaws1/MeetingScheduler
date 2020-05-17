package com.example.xinshen.comp2100_meetingschedule.main;

// meeting object class
public class MeetingModel {
	private int id;
	private int star_time;
	private int end_time;
	private int day;
	private String start_time_str ="";
	private String end_time_str ="";
	private String name="";
	private String descripsion ="";
	private String room ="";
	private String venue ="";

	@Override
	public String toString() {
		return "TimeTableModel [id=" + id + ", startnum=" + star_time
				+ ", endnum=" + end_time + ", week=" + day + ", starttime="
				+ start_time_str + ", endtime=" + end_time_str + ", name=" + name
				+ ", teacher=" + descripsion + ", classroom=" + room
				+ ", weeknum=" + venue + "]";
	}

	public int getId() {
		return id;
	}

	public int getStar_time() {
		return star_time;
	}

	public int getEnd_time() {
		return end_time;
	}

	public int getDay() {
		return day;
	}

	public String getStart_time_str() {
		return start_time_str;
	}

	public String getEnd_time_str() {
		return end_time_str;
	}

	public String getName() {
		return name;
	}

	public String getDescripsion() {
		return descripsion;
	}

	public String getRoom() {
		return room;
	}

	public String getVenue() {
		return venue;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStar_time(int star_time) {
		this.star_time = star_time;
	}

	public void setEnd_time(int end_time) {
		this.end_time = end_time;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setStart_time_str(String start_time_str) {
		this.start_time_str = start_time_str;
	}

	public void setEnd_time_str(String end_time_str) {
		this.end_time_str = end_time_str;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescripsion(String descripsion) {
		this.descripsion = descripsion;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}
	public MeetingModel() {
		// TODO Auto-generated constructor stub
	}

	public MeetingModel(int id, int start_time, int end_time, int day,
						String start_time_str, String end_time_str, String name, String descripsion,
						String room, String venue) {
		super();
		this.id = id;
		this.star_time = start_time;
		this.end_time = end_time;
		this.day = day;
		this.start_time_str = start_time_str;
		this.end_time_str = end_time_str;
		this.name = name;
		this.descripsion = descripsion;
		this.room = room;
		this.venue = venue;
	}

}
