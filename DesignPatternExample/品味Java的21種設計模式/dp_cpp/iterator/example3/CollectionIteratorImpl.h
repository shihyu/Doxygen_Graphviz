#pragma once

#include "Iterator.h"
#include "PayManager.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example3
				{


					///
					/// <summary> * ����ʵ�ַ���Collection���ϵĵ����ӿڣ�Ϊ���ⲿͳһ���ʷ�ʽ </summary>
					/// 
					class CollectionIteratorImpl : public Iterator
					{
					///	
					///	 <summary> * ������ű������ľۺ϶��� </summary>
					///	 
					private:
						PayManager *aggregate;
					///	
					///	 <summary> * ������¼��ǰ��������λ������
					///	 * -1��ʾ�տ�ʼ��ʱ�򣬵�����ָ��ۺ϶����һ������֮ǰ </summary>
					///	 
						int index;

					public:
						CollectionIteratorImpl(PayManager *aggregate);

						virtual void first();
						virtual void next();
						virtual bool isDone();
						virtual object *currentItem();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								index = -1;

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}