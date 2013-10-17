#pragma once

#include "Builder.h"
#include "Product.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace builder
			{
				namespace example2
				{
					///
					/// <summary> * ����Ĺ�����ʵ�ֶ��� </summary>
					/// 
					class ConcreteBuilder : public Builder
					{
					///	
					///	 <summary> * ���������չ����Ĳ�Ʒ���� </summary>
					///	 
					private:
						Product *resultProduct;
					///	
					///	 <summary> * ��ȡ���������չ����Ĳ�Ʒ���� </summary>
					///	 * <returns> ���������չ����Ĳ�Ʒ���� </returns>
					///	 
					public:
						virtual Product *getResult();
						virtual void buildPart();
					};
				}
			}
		}
	}
}