package com.example.ghifar.footballmatch.view.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.eventleaguemodel.Event
import com.example.ghifar.footballmatch.presenter.*
import com.example.ghifar.footballmatch.presenter.nextprev.DataMatchInterface
import com.example.ghifar.footballmatch.presenter.nextprev.NextMatchPresenter
import com.example.ghifar.footballmatch.view.activity.DetailMatchActivity
import com.example.ghifar.footballmatch.view.adapter.MatchAdapter
import android.content.ContentValues
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 *
 */
class NextMatchFragment : Fragment(), DataMatchInterface {

    private val TAG = NextMatchFragment::class.java.simpleName

    private var items: List<Event> = mutableListOf()

    private lateinit var recyclerListTeam: RecyclerView
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var adapter: MatchAdapter
    private lateinit var onArtikelClickListener: MatchAdapter.OnArtikelClickListener
    private lateinit var onNotificationClickListener: MatchAdapter.OnNotificationClickListener
    private lateinit var presenter: NextMatchPresenter

    private var leagueId = "4328"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_next_match, container,
                false)

        recyclerListTeam = v.findViewById(R.id.recyclerListTeam)
        swiperefresh = v.findViewById(R.id.swiperefresh)

        presenter = NextMatchPresenter(this)

        listenerFunction()
        presenter.getDataNextMatch(leagueId)

        return v
    }

    override fun onPause() {
        super.onPause()
        presenter.disposeThis()
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading")
        swiperefresh.isRefreshing = true
    }

    override fun hideLoading() {
        Log.d(TAG, "hideLoading")
        swiperefresh.isRefreshing = false
    }

    override fun showEventList(data: List<Event>) {
        Log.d(TAG, "showEventList")
        items = data
        setRecyclerView()
    }

    override fun showError(msg: String) {
        Log.d(TAG, "showError")
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setRecyclerView() {
        swiperefresh.isRefreshing = false
        recyclerListTeam.layoutManager = LinearLayoutManager(context)
        adapter = MatchAdapter(items, context, Constant.NEXTMATCH)
        adapter.setOnArtikelClickListener(onArtikelClickListener)
        adapter.setOnNotificationClickListener(onNotificationClickListener)
        recyclerListTeam.adapter = adapter
    }

    fun addAppointmentsToCalender(curActivity: Context, title: String,
                                  desc: String, place: String, status: Int, startDate: Long,
                                  needReminder: Boolean, needMailService: Boolean): Long {
        /***************** Event: add event  */
        var eventID: Long = -1
        try {
            val eventUriString = "content://com.android.calendar/events"
            val eventValues = ContentValues()
            eventValues.put("calendar_id", 1) // id, We need to choose from
            // our mobile for primary its 1
            eventValues.put("title", title)
            eventValues.put("description", desc)
            eventValues.put("eventLocation", place)

            val endDate = startDate + 1000 * 10 * 10 // For next 10min
            eventValues.put("dtstart", startDate)
            eventValues.put("dtend", endDate)

            // values.put("allDay", 1); //If it is bithday alarm or such
            // kind (which should remind me for whole day) 0 for false, 1
            // for true
            eventValues.put("eventStatus", status) // This information is
            // sufficient for most
            // entries tentative (0),
            // confirmed (1) or canceled
            // (2):
            eventValues.put("eventTimezone", "UTC/GMT +5:30")
            /*
      * Comment below visibility and transparency column to avoid
      * java.lang.IllegalArgumentException column visibility is invalid
      * error
      */
            // eventValues.put("allDay", 1);
            // eventValues.put("visibility", 0); // visibility to default (0),
            // confidential (1), private
            // (2), or public (3):
            // eventValues.put("transparency", 0); // You can control whether
            // an event consumes time
            // opaque (0) or transparent (1).

            eventValues.put("hasAlarm", 1) // 0 for false, 1 for true

            val eventUri = curActivity.applicationContext
                    .contentResolver
                    .insert(Uri.parse(eventUriString), eventValues)
            eventID = java.lang.Long.parseLong(eventUri!!.lastPathSegment)

            if (needReminder) {
                /***************** Event: Reminder(with alert) Adding reminder to event ***********         */

                val reminderUriString = "content://com.android.calendar/reminders"
                val reminderValues = ContentValues()
                reminderValues.put("event_id", eventID)
                reminderValues.put("minutes", 5) // Default value of the
                // system. Minutes is a integer
                reminderValues.put("method", 1) // Alert Methods: Default(0),
                // Alert(1), Email(2),SMS(3)

                val reminderUri = curActivity.applicationContext
                        .contentResolver
                        .insert(Uri.parse(reminderUriString), reminderValues)
            }

            /***************** Event: Meeting(without alert) Adding Attendies to the meeting  */

            if (needMailService) {
                val attendeuesesUriString = "content://com.android.calendar/attendees"
                /********
                 * To add multiple attendees need to insert ContentValues
                 * multiple times
                 */
                val attendeesValues = ContentValues()
                attendeesValues.put("event_id", eventID)
                attendeesValues.put("attendeeName", "xxxxx") // Attendees name
                attendeesValues.put("attendeeEmail", "yyyy@gmail.com")// Attendee Email
                attendeesValues.put("attendeeRelationship", 0) // Relationship_Attendee(1),
                // Relationship_None(0),
                // Organizer(2),
                // Performer(3),
                // Speaker(4)
                attendeesValues.put("attendeeType", 0) // None(0), Optional(1),
                // Required(2),
                // Resource(3)
                attendeesValues.put("attendeeStatus", 0) // NOne(0),
                // Accepted(1),
                // Decline(2),
                // Invited(3),
                // Tentative(4)

                val eventsUri = Uri.parse("content://calendar/events")
                val url = curActivity.applicationContext
                        .contentResolver
                        .insert(eventsUri, attendeesValues)

                // Uri attendeuesesUri = curActivity.getApplicationContext()
                // .getContentResolver()
                // .insert(Uri.parse(attendeuesesUriString), attendeesValues);
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error in adding event on calendar" + ex.message)
        }

        return eventID

    }


    private fun listenerFunction() {
        swiperefresh.setOnRefreshListener{
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout")
            presenter.getDataNextMatch(leagueId)
        }

        onArtikelClickListener = object : MatchAdapter.OnArtikelClickListener {
            override fun onClick(posisi: Int, model: Event) {
                Log.d(TAG, "NextMatch clicked on position : $posisi")
                val intent = Intent(context, DetailMatchActivity::class.java)
                intent.putExtra(Constant.ID, items.get(posisi).idEvent)
                intent.putExtra(Constant.POSITION, posisi)
                intent.putExtra(Constant.HOMETEAMNAME, items.get(posisi).idHomeTeam)
                intent.putExtra(Constant.AWAYTEAMNAME, items.get(posisi).idAwayTeam)
                intent.putExtra(Constant.TIPEMATCH, Constant.NEXTMATCH)
                startActivity(intent)
            }
        }

        onNotificationClickListener = object : MatchAdapter.OnNotificationClickListener {
            override fun onClick(posisi: Int, model: Event) {
                Log.d(TAG, "notification clicked on position : $posisi")
                val intent = Intent(Intent.ACTION_INSERT)
                intent.type = "vnd.android.cursor.item/event"

                val calendar = Calendar.getInstance()
                var year = model.dateEvent?.substring(0,4)?.toInt()
                var month = model.dateEvent?.substring(5,7)?.toInt()
                var date = model.dateEvent?.substring(8,10)?.toInt()
                var hour = model.strTime?.substring(0,2)?.toInt()
                var minute = model.strTime?.substring(3,5)?.toInt()
                var second = model.strTime?.substring(6,8)?.toInt()
                calendar.set(year!!, month!!, date!!, hour!!, minute!!, second!!)

                val startTime: Long = calendar.timeInMillis
                val endTime: Long = startTime + TimeUnit.MINUTES.toMillis(90)

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)

                intent.putExtra(CalendarContract.Events.TITLE,
                        "${model.strHomeTeam} vs ${model.strAwayTeam}")

                intent.putExtra(CalendarContract.Events.DESCRIPTION,
                        "Reminder for match between ${model.strHomeTeam} and ${model.strAwayTeam}")

                startActivity(intent)
            }
        }
    }

}
