#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace observer
			{
				namespace example3
				{
					///
					/// <summary> * �۲��ߣ����籨ֽ�Ķ��� </summary>
					/// 
					class Observer
					{
					///	
					///	 <summary> * ��֪ͨ�ķ�����ֱ�Ӱѱ�ֽ���������͹��� </summary>
					///	 * <param name="content"> ��ֽ������ </param>
					///	 
					public:
						virtual public void update(std::string) = 0;
					};

				}
			}
		}
	}
}