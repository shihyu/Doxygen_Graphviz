#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace abstractfactory
			{
				namespace example4
				{

					///
					/// <summary> * ����չ�ĳ��󹤳��Ľӿ� </summary>
					/// 
					class AbstractFactory
					{
					///	
					///	 <summary> * һ��ͨ�õĴ�����Ʒ����ķ�����Ϊ�˼򵥣�ֱ�ӷ���Object
					///	 * Ҳ����Ϊ���б������Ĳ�Ʒ����һ�������Ľӿ� </summary>
					///	 * <param name="type"> ���崴���Ĳ�Ʒ���ͱ�ʶ </param>
					///	 * <returns> �������Ĳ�Ʒ���� </returns>
					///	 
					public:
						virtual public object *createProduct(int) = 0;
					};
				}
			}
		}
	}
}