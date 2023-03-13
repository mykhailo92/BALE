from keras import Input, Model
from keras.callbacks import EarlyStopping
from keras.layers import Conv2D, Flatten, Dense, AveragePooling2D, concatenate
from keras.optimizers import Adam


def create_basic_model():
    image_input = Input((36, 120, 3), name="Image_Input")
    coordinates_input = Input((8,), name="Coordnates_Input")
    image_layer = Conv2D(16, 3, 2, activation='relu')(image_input)
    image_layer = AveragePooling2D()(image_layer)
    image_layer = Conv2D(32, 2, 2, activation='relu')(image_layer)
    image_layer = AveragePooling2D()(image_layer)
    image_layer = Conv2D(64, 2, 2, activation='relu')(image_layer)
    image_layer = Flatten()(image_layer)
    image_model = Model(inputs=image_input, outputs=image_layer, name="Image_Model")

    coordinates_layer = Dense(64, activation='relu')(coordinates_input)
    coordinates_layer = Dense(32, activation='relu')(coordinates_layer)
    coordinates_layer = Dense(16, activation='relu')(coordinates_layer)
    coordinates_model = Model(inputs=coordinates_input, outputs=coordinates_layer, name="Coordinates_Model")

    concat_layer = concatenate([image_model.outputs[0], coordinates_model.outputs[0]])
    concat_layer = Dense(32, activation='relu')(concat_layer)
    concat_layer = Dense(16, activation='relu')(concat_layer)
    concat_layer = Dense(2, activation='sigmoid')(concat_layer)

    model = Model(inputs=[image_input, coordinates_input],
                  outputs=concat_layer,
                  name="Basic_Model")
    opt = Adam(learning_rate=0.0001)
    model.compile(optimizer=opt, loss="mean_squared_error", metrics=['accuracy'])

    return model


def fit_basic_model(images_and_coordinates, positions, epochs):
    model = create_basic_model()
    callback = EarlyStopping(monitor='loss', patience=20, min_delta=0, verbose=1, restore_best_weights=True)
    model.fit(images_and_coordinates, positions, batch_size=32, epochs=epochs, callbacks=[callback], verbose=0)
    return model
