#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace strategy
			{
				namespace example6
				{
					///
					/// <summary> * ��־��¼���ԵĽӿ� </summary>
					/// 
					class LogStrategy
					{
					///	
					///	 <summary> * ��¼��־ </summary>
					///	 * <param name="msg"> ���¼����־��Ϣ </param>
					///	 
					public:
						virtual public void log(std::string) = 0;
					};

				}
			}
		}
	}
}