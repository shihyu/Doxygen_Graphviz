#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace prototype
			{
				namespace example6
				{
					///
					/// <summary> * ����һ����¡��Ʒ����Ľӿ�
					/// * </summary>
					/// 
					class ProductPrototype
					{
					///	
					///	 <summary> * ��¡��Ʒ����ķ��� </summary>
					///	 * <returns> һ���������¡�����Ĳ�Ʒ���� </returns>
					///	 
					public:
						virtual public ProductPrototype *cloneProduct() = 0;
					};

				}
			}
		}
	}
}