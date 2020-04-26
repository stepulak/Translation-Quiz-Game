require "class"

keyboard = class:new()

local KEYBOARD_WIDTH_Q = 0.8
local KEYBOARD_HEIGHT_Q = 0.45
local KEY_OFFSET_WIDTH_Q = 0.005
local KEY_OFFSET_HEIGHT_Q = 0.005
local KEYS_PER_WIDTH = 5
local KEYS_PER_HEIGHT = 5
local SHUFFLE_TIME = 3
local SHUFFLE_MAX = 3
local ANIMATION_TIME = 1

local KEYSETS = {
	DE = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜß"
}

function keyboard:init(cam, keyset_index)
	self.cam = cam
	self.keyset = KEYSETS[keyset_index]

	self:setup_key_sizes()
	self:init_keys()
end

function keyboard:init_keys()
	self.keys = {}

	for i=0, KEYS_PER_WIDTH-1 do
		self.keys[i] = {}
		
		for j=0, KEYS_PER_HEIGHT-1 do
			self.keys[i][j] = {
				letter = "X",
				animation_timer = 0,
				animation_type = nil
			}
		end
	end
end

function keyboard:get_random_key()
	return self.keyset[love.math.random(1, #self.keyset)]
end

function keyboard:setup_key_sizes()
	local keyboard_width = self.cam.virtual.width * KEYBOARD_WIDTH_Q
	local keyboard_height = self.cam.virtual.height * KEYBOARD_HEIGHT_Q
	local key_offset_width = self.cam.virtual.width * KEY_OFFSET_WIDTH_Q
	local key_offset_height = self.cam.virtual.height * KEY_OFFSET_HEIGHT_Q
	
	self.key_width = (keyboard_width - 2 * KEYS_PER_WIDTH * key_offset_width) / KEYS_PER_WIDTH
	self.key_height = (keyboard_height - 2 * KEYS_PER_HEIGHT * key_offset_height) / KEYS_PER_HEIGHT
end

function keyboard:get_key(x, y)
	if x >= 0 and x < KEYS_PER_WIDTH and 
		y >= 0 and y < KEYS_PER_HEIGHT then
		return self.keys[x][y]
	end
	return nil
end

-- @returns:
-- 		false, false if out of area
--		true, false if in area but found bad letter
--		true, true if in area and found correct letter
function keyboard:click(relative_x, relative_y, expected_letter)
	local key = self:get_key(
		math.fmod(relative_x, self.key_width), 
		math.fmod(relative_y, self.key_height))

	if key == nil then
		return false, false
	end
	if key.animation_type ~= nil then
		return true, false
	end
	if key.letter ~= expected_letter then
		key.animation_type = "go_red"
		return true, false
	else
	key.animation_type = "fade_out"
	return true, true
end

function keyboard:update(dt)
	for i=0, KEYS_PER_WIDTH-1 do
		for j=0, KEYS_PER_HEIGHT-1 do
			local key = self.keys[i][j]
			
		end
	end
end


function keyboard:draw(cam, font)
	for i=0, KEYS_PER_WIDTH-1 do
		for j=0, KEYS_PER_HEIGHT-1 do
		
		end
	end
end