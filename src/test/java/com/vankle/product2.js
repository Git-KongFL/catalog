var result = {
	"code" : "10000",
	"msg" : "成功",
	"data" : { 
		"manageStock" : 0, //是/否，如果否，则商品默认为无限库存，并且不接受接口的同步库存。 0否 1 是
		"originalPrice" : 359,//商品原始价
		"sellPrice" : 359, //正常的销售价
		"description" : "", //商品展示描述
		"type" : 2, // 1：普通商品 2：捆绑组合 3: 同类组合 4: 系列组合  
		"metaDescription" : "",//商品的营销关键词做详细描述
		"isBackorder" : 0, //是否预定 0否 1 是
		"metaKeywords" : "", //商品用关键词进行描述
		"metaTitle" : "",// 给商品一个用于营销的标题
		"name" : "Miniature Bridge Series Tiara Shaped Designer Promise Ring ",//商品名称
		"spu" : "NRC0478", //sup
		"id" : 367, //商品id
		"catalogProductEntityDiscount" : { //折扣对象
			"discountPercentage" : 0.42,// 折扣
			"discountAmount" : 149,//折扣金额
			"discountEndTime" : "2017-07-21 13:36:00", // 折扣 开始时间 暂时不用
			"discountStartTime" : "2017-07-21 13:36:00" // 折扣 结束时间  暂时不用
		},
		"catalogProductSpecList" : [{  //规格列表 
					"description" : "",
					"name" : "color", // 规则名称 颜色
					"id" : 79, //规格ID
					"value" : "1",  //规则值
					"catalogProductSpecValueList" : [ //规格属性列表
						   {
								"name" : "red", // 规格属性名 红
								"id" : 83, //// 规格属性id
								"value" : "1" // 规格属性值 1
							}, {
								"name" : "golden",
								"id" : 84,
								"value" : "3" 
							}]
				}, {
					"description" : "",
					"name" : "size", // 规则名称 颜色
					"id" : 80, // 规则名称 颜色
					"value" : "2" ,
					"catalogProductSpecValueList" : [//规格属性列表
							{
								"name" : "6(U.S.)\\16.45(mm)",// 规格属性名 红
								"id" : 85,
								"value" : "1"
							}, {
								"name" : "6.5(U.S.)\\16.9(mm)",
								"id" : 86,
								"value" : "2"
							}, {
								"name" : "7(U.S.)\\17.3(mm)",
								"id" : 87,
								"value" : "3" 
							}]
				}], //规格
		"catalogProductEntityImages" : [{
			"smallImage" : "https://vancaro-review-image-video.s3.amazonaws.com/product/image/n/r/nrc0478-2-0706.jpg", //小图
			"remoteUrl" : "https://vancaro-review-image-video.s3.amazonaws.com/product/image/n/r/nrc0478-2-0706.jpg" //大图
		}, {
			"smallImage" : "https://vancaro-review-image-video.s3.amazonaws.com/product/image/n/r/nrc0478-4-0706.jpg",
			"remoteUrl" : "https://vancaro-review-image-video.s3.amazonaws.com/product/image/n/r/nrc0478-4-0706.jpg"
		}],
		"catalogProductEntityVideos" : [{
			"imageUrl" : "http://www.vancaro.com/media/catalog/product/cache/1/thumbnail/171x99/9df78eab33525d08d6e5fb8d27136e95/1/l/1lmrTNg9sOs.jpg",//视频图片
			"name" : "High Quality Cubic Zirconia Rings from VANCARO", //视频名称
			"remoteUrl" : "www.youtube.com/embed/1lmrTNg9sOs"  //youtube 视频地址
		}],
		"catalogProductGroupSell" : {   // 2：捆绑组合 3: 同类组合 4: 系列组合 才有此项
			"name" : "group111",// 组合名称
			"description" : "test",  //组合描述
			"list" : [ {         // 1 简单商品 列表
				"originalPrice" : 349,
				"description" : " ",
				"sellPrice" : 349,
				"type" : 1,
				"metaDescription" : "",
				"isBackorder" : 0,
				"metaKeywords" : "",
				"metaTitle" : "",
				"name" : "Clown Inspired  Light Red Garnet Birthstone Promise Ring For Girlfriend ",
				"spu" : "NRC0475",
				"id" : 360,
				"catalogProductEntityDiscount" : {
					"discountPercentage" : 0.48, 
					"discountEndTime" : "2017-07-21 13:28:29",
					"discountStartTime" : "2017-07-21 13:28:29",
					"discountAmount" : 169 
				},
				"catalogProductSpecList" : [],
				"catalogProductEntityImages" : [{
					"smallImage" : "https://vancaro-review-image-video.s3.amazonaws.com/product/image/n/r/nrc0475-1-0628.jpg",
					"remoteUrl" : "https://vancaro-review-image-video.s3.amazonaws.com/product/image/n/r/nrc0475-1-0628.jpg" 
				} ],
				"catalogProductEntityVideos" : [],
				"catalogProductRecommendeds" : []
			} ]
		},
		"catalogProductRecommendeds" : [{
			"smallImage" : "https://vancaro-review-image-video.s3.amazonaws.com/product/image/n/r/nrc0478-2-0706.jpg",  //推荐商品小图
			"recommendedProductId" : 11//推荐商品id
		}, {
			"smallImage" : "https://vancaro-review-image-video.s3.amazonaws.com/product/image/n/r/nrc0478-2-0706.jpg", //推荐商品小图			
			"recommendedProductId" : 19 //推荐商品id
		} ]
	}
}
