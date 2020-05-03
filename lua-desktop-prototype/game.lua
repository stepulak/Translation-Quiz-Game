require "camera"
require "keyboard"
require "dictionary"
require "input_array"

local KEYBOARD_OFFSET_BOTTOM_Q = 0.02

game = class:new()

local d = load_dictionary("dictionaries/czech_german.txt", "Cesko - Nemecký", "czech", "german")

function game:init(cam)
	self.keyboard = keyboard:new(cam, KEYSET["german"])
	self.input_array = input_array:new(cam, d.words[1][2])
end

function game:keyboard_position(cam)
	local keyboard_w = self.keyboard:get_width()
	local keyboard_h = self.keyboard:get_height()
	local offset_bottom = cam.virtual_h * KEYBOARD_OFFSET_BOTTOM_Q
	
	return (cam.virtual_w - keyboard_w) / 2,
		cam.virtual_h - keyboard_h - offset_bottom
end

function game:mouse_released(cam, x, y)
	local kx, ky = self:keyboard_position(cam)
	kx = x - kx
	ky = y - ky
	self.keyboard:click(kx, ky, "A")
end

function game:update(dt)
	self.keyboard:update(dt)
end

function game:draw(cam, font)
	love.graphics.setBackgroundColor(221/255, 219/255, 222/255, 1)
	
	-- Draw keyboard
	local keyboard_x, keyboard_y = self:keyboard_position(cam)
	love.graphics.push()
	love.graphics.translate(keyboard_x, keyboard_y)
	self.keyboard:draw(cam, font)
	love.graphics.pop()
end