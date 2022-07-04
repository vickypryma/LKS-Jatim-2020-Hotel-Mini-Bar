package com.pryma.hotelminibar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.pryma.hotelminibar.model.Checkout
import com.pryma.hotelminibar.model.Food
import com.pryma.hotelminibar.model.Room
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class MainActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var apiCall: ApiInterface
    private lateinit var spinnerRoom: Spinner
    private lateinit var rgType: RadioGroup
    private lateinit var spinnerFood: Spinner
    private lateinit var txtPrice: EditText
    private lateinit var txtQty: EditText
    private lateinit var txtTotal: EditText
    private lateinit var btnSubmit: Button
    private var roomList: List<Room>? = null
    private var foodList: List<Food>? = null

    private var roomID: Int? = null
    private var foodPrice = 0
    private var foodQty = 0
    private var foodTotal = 0
    private var food: Food? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiCall = ApiInterface.create()

        spinnerRoom = findViewById(R.id.spinnerRoom)
        rgType = findViewById(R.id.rgType)
        spinnerFood = findViewById(R.id.spinnerFood)
        txtPrice = findViewById(R.id.txtPrice)
        txtQty = findViewById(R.id.txtQty)
        txtTotal = findViewById(R.id.txtTotal)
        btnSubmit = findViewById(R.id.btnSubmit)

        rgType.setOnCheckedChangeListener(this)
        spinnerRoom.onItemSelectedListener = this
        spinnerFood.onItemSelectedListener = this
        txtQty.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                foodQty = s.toString().toIntOrNull() ?: 0
                foodTotal = foodPrice * foodQty
                txtTotal.setText(foodTotal.toString())
            }

        })

        btnSubmit.setOnClickListener {
            if (roomID != null) {
                if (food != null && foodQty > 0) {
                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setMessage("Yakin ingin checkout?")
                        .setPositiveButton("Iya") { _, _ -> checkout() }
                        .setNegativeButton("Tidak") { _, _ -> }
                    dialogBuilder.show()
                } else {
                    Toast.makeText(this@MainActivity, "Belum pilih makanan!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "Tidak ada kamar yang dipilih!", Toast.LENGTH_SHORT).show()
            }
        }

        getRooms()
        getFoods()
    }

    private fun getRooms() {
        apiCall.getRooms()
            .enqueue(object : Callback<List<Room>> {
                override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                    val rooms = response.body()
                    val roomNumber = arrayListOf<String>()

                    if (rooms != null) {
                        for (room in rooms)
                            roomNumber.add(room.roomNumber.toString())
                    }

                    val dataAdapter: ArrayAdapter<String> = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, roomNumber)
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerRoom.adapter = dataAdapter

                    roomList = rooms
                }

                override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                    t?.printStackTrace()
                }
            })
    }

    private fun getFoods() {
        apiCall.getFoods()
            .enqueue(object : Callback<List<Food>> {
                override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                    foodList = response.body()
                    setFoodList()
                }

                override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                    t?.printStackTrace()
                }
            })
    }

    private fun setFoodList(type: Char = 'F') {
        val list = foodList?.filter { food -> food.type == type }
        val foodNames = arrayListOf<String>()

        if (list != null) {
            for (food in list)
                foodNames.add(food.name)
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, foodNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFood.adapter = adapter
    }

    private fun checkout() {
        val data = Checkout(null, roomID, food?.id, foodQty, foodTotal)
        apiCall.checkout(data)
            .enqueue(object : Callback<Checkout> {
                override fun onResponse(call: Call<Checkout>, response: Response<Checkout>) {
                    Toast.makeText(this@MainActivity, "Berhasil chekout!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Checkout>, t: Throwable) {
                    t?.printStackTrace()
                }

            })
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (group?.id) {
            R.id.rgType -> {
                if (checkedId == R.id.rbFood) setFoodList()
                else setFoodList('D')
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinnerRoom -> {
                roomID = roomList?.get(position)?.id
            }

            R.id.spinnerFood -> {
                val type = if (rgType.checkedRadioButtonId == R.id.rbFood) 'F' else 'D'
                food = foodList?.filter { f -> f.type == type }?.get(position)
                foodPrice = food?.price ?: 0
                foodQty = txtQty.text.toString().toIntOrNull() ?: 0
                foodTotal = foodPrice * foodQty

                txtPrice.setText(food?.price.toString())
                txtTotal.setText(foodTotal.toString())
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}