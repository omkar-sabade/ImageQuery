# ImageQuery
A custom image search android app 

<img src="https://github.com/omkar-sabade/ImageQuery/blob/master/media/demo.gif" height="850">


Using this app you can search for images through Google's custom search engines.

<p>
<img src="https://github.com/omkar-sabade/ImageQuery/blob/master/media/image_details_page.jpg" height=700">
<img src="https://github.com/omkar-sabade/ImageQuery/blob/master/media/list_view.jpg" height="700">
</p>

## Design
The app uses **Retrofit** to execute GET requests. **DataBinding** is used to load data into the UI components. 
**Glide** is used to load images from URL. I have used the **Room** library for persistence. 
**ViewModels** and **Repository** architecture is used to separate the logic from UI.
Apart from that **Navigation** component is to move between fragments. 

The last searched results are locally saved meaning that no requests are made when you search the same string.
